package me.mattco.serenityos.gml.annotators

import ai.grazie.utils.dropPrefix
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.Type
import me.mattco.serenityos.gml.psi.api.*

class GMLErrorAnnotator : DSLAnnotator() {
    override fun annotate(element: PsiElement) {
        when (element) {
            is GMLWidgetName -> {
                val name = element.text.dropPrefix("@")
                if (element.project.service<GMLService>().lookupWidget(name) == null)
                    element.highlightError("Unknown widget")
            }
            is GMLPropertyIdentifier -> {
                val parentWidget = element.ancestorOfType<GMLWidget>() ?: return
                val gmlService = element.project.service<GMLService>()
                val widget = gmlService.lookupWidget(parentWidget.identWithoutAt) ?: return
                if (widget.getProperty(element.identifier.text, gmlService) == null)
                    element.highlightError("Unknown property")
            }
            is GMLProperty -> {
                val type = element.gmlProperty?.type ?: return
                val value = element.value ?: return
                lintType(type, value)?.let {
                    it.targetElement.highlightError(it.error)
                }
            }
        }
    }

    private data class Lint(val targetElement: PsiElement, val error: String)

    private fun lintType(type: Type, value: GMLValue): Lint? {
        when (type) {
            is Type.Array -> {
                if (value.array == null)
                    return Lint(value, "Expected Array")

                if (value.array!!.valueList.size !in type.min..type.max) {
                    if (type.min == type.max)
                        return Lint(value, "Expected Array length to have ${type.min} elements")
                    return Lint(value, "Expected Array length to have ${type.min}-${type.max} elements")
                }

                for (element in value.array!!.valueList) {
                    lintType(type.inner, element)?.let { return it }
                }
            }
            Type.Bitmap -> if (value.string == null) return Lint(value, "Expected String")
            Type.Bool -> if (value.boolean == null) return Lint(value, "Expected bool")
            Type.Color -> if (value.string == null) return Lint(value, "Expected String")
            Type.Widget -> if (value.widget == null) return Lint(value, "Expected Widget")
            Type.Double -> if (value.number == null) return Lint(value, "Expected double")
            is Type.EnumType -> if (value.string == null) return Lint(value, "Expected String")
            is Type.ErrorType -> return Lint(value, type.message)
            is Type.Int -> {
                if (value.number == null)
                    return Lint(value, "Expected ${type.presentation()}")
                if (!type.signed && value.number!!.text.startsWith('-'))
                    return Lint(value, "Unsigned int cannot hold a negative number")
            }
            Type.Margins -> return lintType(Type.Array(1, 4, Type.Int(true)), value)
            Type.String -> if (value.string == null) return Lint(value, "Expected String")
            Type.UIDimension -> {
                if (value.string != null) {
                    if (value.string!!.text !in uiDimEnumValues)
                        return Lint(value, "Invalid enum value, valid value are: $uiDimEnumValuesForDisplay")
                } else if (value.number == null) {
                    return Lint(value, "Expected an i64 or one of: $uiDimEnumValuesForDisplay")
                } else {
                    return lintType(Type.Int(false), value)
                }
            }
            is Type.Variant -> {
                if (type.types.all { lintType(it, value) != null })
                    return Lint(value, "Expected one of: ${type.presentation()}")
            }
        }

        return null
    }

    companion object {
        private val uiDimEnumValues = setOf("grow", "opportunistic_grow", "fit", "shrink")
        private val uiDimEnumValuesForDisplay = uiDimEnumValues.joinToString { "\"$it\"" }
    }
}
