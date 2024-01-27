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
import me.mattco.serenityos.gml.Widget
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.psi.GMLNamedElement
import me.mattco.serenityos.gml.psi.api.GMLWidget
import me.mattco.serenityos.gml.psi.singleRef

abstract class GMLWidgetMixin(node: ASTNode) : GMLNamedElement(node), GMLWidget {
    override val gmlWidget: Widget?
        get() = project.service<GMLService>().lookupWidget(identWithoutAt)

    override val identWithoutAt by lazy { widgetName.text.dropPrefix("@") }

    override fun getReference() = singleRef { resolveCppDecl() }

    override fun getNameIdentifier() = widgetName

    private fun resolveCppDecl(): OCElement? {
        val nameParts = widgetName.findChildrenOfType(GMLTypes.IDENTIFIER).mapTo(mutableListOf()) { it.text }
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
