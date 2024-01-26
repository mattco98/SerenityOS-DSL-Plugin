package me.mattco.serenityos.gml.completions

import ai.grazie.utils.dropPrefix
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.common.psiElement
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.Type
import me.mattco.serenityos.gml.psi.api.GMLComponent
import me.mattco.serenityos.gml.psi.api.GMLPropertyIdentifier

object GMLPropertyCompletion : GMLCompletion() {
    override val pattern: ElementPattern<out PsiElement>
        get() = psiElement(GMLTypes.IDENTIFIER).withParent(psiElement<GMLPropertyIdentifier>())

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val gmlService = parameters.editor.project!!.service<GMLService>()
        val parentComponentName =
            parameters.position.ancestorOfType<GMLComponent>()?.componentName?.text?.dropPrefix("@") ?: return
        val parentComponent = gmlService.lookupComponent(parentComponentName) ?: return
        val elements = parentComponent.properties.map { property ->
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
