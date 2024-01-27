package me.mattco.serenityos.gml.annotators

import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.common.findChildrenOfType
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.Highlights
import me.mattco.serenityos.gml.psi.api.GMLWidget
import me.mattco.serenityos.gml.psi.api.GMLProperty
import me.mattco.serenityos.gml.psi.api.GMLValue

class GMLSyntaxAnnotator : DSLAnnotator(), DumbAware {
    override fun annotate(element: PsiElement) {
        when (element) {
            is GMLValue -> {
                if (element.string != null) {
                    val lastChar = element.string!!.text.lastOrNull()
                    if (lastChar != null && lastChar != '"')
                        element.highlightError("Unexpected EOL in string literal")
                }
            }
            is GMLWidget -> {
                val parts = element.widgetName.findChildrenOfType(GMLTypes.IDENTIFIER)
                parts.dropLast(1).forEach { it.highlight(Highlights.NAMESPACE_NAME) }
                if (parts.isNotEmpty())
                    parts.last().highlight(Highlights.WIDGET_NAME)
            }
            is GMLProperty -> element.propertyIdentifier.identifier.highlight(Highlights.PROPERTY_NAME)
        }
    }
}
