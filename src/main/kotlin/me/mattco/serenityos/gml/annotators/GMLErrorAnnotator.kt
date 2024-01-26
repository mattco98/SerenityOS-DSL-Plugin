package me.mattco.serenityos.gml.annotators

import ai.grazie.utils.dropPrefix
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.common.findChildOfType
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.psi.api.GMLComponent
import me.mattco.serenityos.gml.psi.api.GMLComponentName
import me.mattco.serenityos.gml.psi.api.GMLPropertyIdentifier

class GMLErrorAnnotator : DSLAnnotator() {
    override fun annotate(element: PsiElement, holder: Holder) = with(holder) {
        when (element) {
            is GMLComponentName -> {
                val name = element.text.dropPrefix("@")
                if (element.project.service<GMLService>().lookupComponent(name) == null)
                    element.highlightError("Unknown component")
            }
            is GMLPropertyIdentifier -> {
                val parentWidget = element.ancestorOfType<GMLComponent>() ?: return@with
                val parentName = parentWidget.findChildOfType<GMLComponentName>()?.text?.dropPrefix("@") ?: return@with
                val component = element.project.service<GMLService>().lookupComponent(parentName) ?: return@with
                if (component.properties.none { it.name == element.identifier.text })
                    element.highlightError("Unknown property")
            }
        }
    }
}
