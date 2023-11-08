package me.mattco.serenityos.idl.project

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.toNioPathOrNull
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScopes
import com.jetbrains.cidr.lang.psi.OCFile
import com.jetbrains.cidr.lang.psi.OCStructLike
import me.mattco.serenityos.idl.descendentsOfType
import me.mattco.serenityos.idl.IDLFile
import me.mattco.serenityos.idl.IDLResolver
import me.mattco.serenityos.idl.psi.IDLDeclaration
import java.nio.file.Path
import kotlin.io.path.name

// TODO: I don't really need this, but it might be useful in the future so I'm keeping it around
class IDLProjectServiceImpl(private val project: Project) : IDLProjectService {
    private var libWebFolder: VirtualFile? = null

    override fun resolveImportedFile(from: VirtualFile, path: Path): IDLFile? {
        if (DumbService.isDumb(project))
            return null

        if (libWebFolder == null) {
            var file = from
            while (file.name != "LibWeb" || !file.isDirectory)
                file = file.parent ?: return null
            libWebFolder = file
        }

        val scope = GlobalSearchScopes.directoryScope(project, libWebFolder!!, true)
        val virtualFiles = FilenameIndex.getVirtualFilesByName(path.name, scope)
        val file = virtualFiles.firstOrNull {
            it.toNioPathOrNull()?.endsWith(path) == true
        } ?: return null
        return PsiManager.getInstance(project).findFile(file) as? IDLFile
    }

    override fun findIDLDeclarations(directory: VirtualFile, name: String): Set<IDLDeclaration> {
        if (DumbService.isDumb(project))
            return emptySet()

        val scope = GlobalSearchScopes.directoryScope(project, directory, true)
        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "idl", scope)
        val declarations = mutableSetOf<IDLDeclaration>()
        val manager = PsiManager.getInstance(project)
        virtualFiles.forEach {
            val file = manager.findFile(it) as? IDLFile ?: return@forEach
            val decl = IDLResolver(file).findDeclaration(name)
            if (decl != null)
                declarations.add(decl)
        }
        return declarations
    }

    override fun findCppDeclarations(directory: VirtualFile, name: String): Set<OCStructLike> {
        if (DumbService.isDumb(project))
            return emptySet()

        val scope = GlobalSearchScopes.directoryScope(project, directory, true)
        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "h", scope)
        val declarations = mutableSetOf<OCStructLike>()
        val manager = PsiManager.getInstance(project)
        for (virtualFile in virtualFiles) {
            val file = manager.findFile(virtualFile) as? OCFile ?: continue
            for (decl in file.descendentsOfType<OCStructLike>()) {
                if (decl.name == name && hasWebNamespace(decl))
                    declarations.add(decl)
            }
        }

        return declarations
    }

    private fun hasWebNamespace(struct: OCStructLike): Boolean {
        var ns = struct.namespaceQualifier
        while (ns != null) {
            if (ns.name == "Web")
                return true
            ns = ns.namespaceQualifier
        }
        return false
    }
}
