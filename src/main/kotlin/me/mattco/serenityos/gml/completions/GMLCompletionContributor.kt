package me.mattco.serenityos.gml.completions

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

class GMLCompletionContributor : CompletionContributor() {
    init {
        extend(GMLComponentCompletion)
        extend(GMLPropertyCompletion)
    }

    private fun extend(completion: GMLCompletion) =
        extend(CompletionType.BASIC, completion.pattern, completion)
}
