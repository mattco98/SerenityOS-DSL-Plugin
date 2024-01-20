package me.mattco.serenityos.ipc.project

import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.cidr.lang.psi.OCFile
import java.nio.file.Path

class IPCProjectServiceImpl(private val project: Project) : IPCProjectService {
    private var includeRoots: List<VirtualFile>? = null

    override fun resolveImportedFile(path: Path): OCFile? {
        if (DumbService.isDumb(project))
            return null

        for (root in getRoots()) {
            val file = path.fold(root as VirtualFile?) { f, name -> f?.findChild(name.toString()) } ?: continue
            return PsiManager.getInstance(project).findFile(file) as? OCFile ?: continue
        }

        return null
    }

    private fun getRoots(): List<VirtualFile> {
        if (includeRoots == null) {
            val base = project.getBaseDirectories().singleOrNull() ?: return emptyList()
            val userland = base.findChild("Userland")
            includeRoots = listOfNotNull(
                base,
                base.findChild("AK"),
                userland,
                userland?.findChild("Libraries"),
                userland?.findChild("Services"),
            )
        }

        return includeRoots!!
    }
}
