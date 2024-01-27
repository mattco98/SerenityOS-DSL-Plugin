package me.mattco.serenityos.gml.psi.mixins

import ai.grazie.utils.dropPrefix
import com.intellij.lang.ASTNode
import com.intellij.openapi.components.service
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.cidr.lang.psi.OCElement
import com.jetbrains.cidr.lang.psi.OCStructLike
import me.mattco.serenityos.common.findChildrenOfType
import me.mattco.serenityos.gml.Component
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.psi.GMLNamedElement
import me.mattco.serenityos.gml.psi.api.GMLComponent
import me.mattco.serenityos.gml.psi.singleRef

abstract class GMLComponentMixin(node: ASTNode) : GMLNamedElement(node), GMLComponent {
    override val gmlComponent: Component?
        get() = project.service<GMLService>().lookupComponent(identWithoutAt)

    override val identWithoutAt by lazy { componentName.text.dropPrefix("@") }

    override fun getReference() = singleRef { resolveCppDecl() }

    override fun getNameIdentifier() = componentName

    private fun resolveCppDecl(): OCElement? {
        val nameParts = componentName.findChildrenOfType(GMLTypes.IDENTIFIER).mapTo(mutableListOf()) { it.text }
        if (nameParts.isEmpty())
            return null

        val psiManager = PsiManager.getInstance(project)

        var startDir: VirtualFile? = if (nameParts[0] == "GUI" || nameParts[0] == "WebView") {
            nameParts[0] = "Lib${nameParts[0]}"
            project.getBaseDirectories().singleOrNull()?.findChild("Userland")?.findChild("Libraries")
        } else {
            var currFile: VirtualFile? = containingFile.virtualFile
            var nextFile = currFile!!.parent
            while (nextFile.name != "Userland") {
                currFile = nextFile
                nextFile = nextFile.parent
            }

            currFile
        } ?: return null

        for ((index, part) in nameParts.withIndex()) {
            startDir = startDir?.findChild(if (index == nameParts.lastIndex) "$part.h" else part)
        }

        val cppFile = psiManager.findFile(startDir ?: return null) ?: return null

        for (struct in PsiTreeUtil.findChildrenOfType(cppFile, OCStructLike::class.java)) {
            if (struct.name == nameParts.last())
                return struct
        }

        return null
    }
}
