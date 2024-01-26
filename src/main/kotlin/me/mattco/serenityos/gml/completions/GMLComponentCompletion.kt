package me.mattco.serenityos.gml.completions

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressManager
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import me.mattco.serenityos.common.PsiPattern
import me.mattco.serenityos.common.psiElement
import me.mattco.serenityos.gml.GMLService
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.psi.api.GMLComponentName

object GMLComponentCompletion : GMLCompletion() {
    override val pattern: PsiPattern
        get() = psiElement(GMLTypes.IDENTIFIER).withParent(psiElement<GMLComponentName>())

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        ProgressManager.checkCanceled()
        try {
            val elements = parameters.editor.project!!.service<GMLService>().components.values.map {
                LookupElementBuilder.create("@${it.name}")
            }
            result.addAllElements(elements)
        } catch (e: Throwable) {
            throw e
        }
    }
}
