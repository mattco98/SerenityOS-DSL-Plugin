package me.mattco.serenityos.gml.completions

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

abstract class GMLCompletion : CompletionProvider<CompletionParameters>() {
    abstract val pattern: ElementPattern<out PsiElement>
}
