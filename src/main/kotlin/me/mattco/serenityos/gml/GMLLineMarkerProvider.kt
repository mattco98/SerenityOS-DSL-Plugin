package me.mattco.serenityos.gml

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.jetbrains.cidr.lang.psi.OCStructLike
import me.mattco.serenityos.common.findChildOfType
import me.mattco.serenityos.common.findChildrenOfType
import me.mattco.serenityos.gml.psi.api.GMLComponent

class GMLLineMarkerProvider : LineMarkerProvider {
    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        elements.filterIsInstance<GMLComponent>().forEach {
            NavigationGutterIconBuilder.create(AllIcons.Nodes.ModelClass)
                .setTarget((it.reference?.resolve() as? OCStructLike)?.nameIdentifier ?: return@forEach)
                .setTooltipText("Jump to C++ Widget")
                .createLineMarkerInfo(it.componentName.findChildrenOfType(GMLTypes.IDENTIFIER).first())
                .let(result::add)
        }
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null
}
