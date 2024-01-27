package me.mattco.serenityos.gml.completions

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.util.ProcessingContext
import me.mattco.serenityos.common.PsiPattern
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.common.psiElement
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.Type
import me.mattco.serenityos.gml.psi.api.GMLWidget
import me.mattco.serenityos.gml.psi.api.GMLPropertyIdentifier

object GMLPropertyCompletion : GMLCompletion() {
    override val pattern: PsiPattern
        get() = psiElement(GMLTypes.IDENTIFIER).withParent(psiElement<GMLPropertyIdentifier>())

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val gmlService = parameters.editor.project!!.service<GMLService>()
        val parentWidgetName =
            parameters.position.ancestorOfType<GMLWidget>()?.identWithoutAt ?: return
        val parentWidget = gmlService.lookupWidget(parentWidgetName) ?: return
        val elements = parentWidget.getAllProperties(gmlService).map { property ->
            LookupElementBuilder.create(property.name)
                .withTypeText(property.type.presentation())
                .withInsertHandler { context, _ ->
                    context.document.insertString(context.selectionEndOffset, ": ")
                    context.editor.caretModel.moveCaretRelatively(2, 0, false, false, false)
                    if (property.type == Type.String) {
                        context.document.insertString(context.selectionEndOffset, "\"\"")
                        context.editor.caretModel.moveCaretRelatively(1, 0, false, false, false)
                    } else if (property.type is Type.Array || property.type is Type.Margins) {
                        context.document.insertString(context.selectionEndOffset, "[]")
                        context.editor.caretModel.moveCaretRelatively(1, 0, false, false, false)
                    }
                }
        }
        result.addAllElements(elements)
    }
}
