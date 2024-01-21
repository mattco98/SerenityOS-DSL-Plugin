package me.mattco.serenityos.gml

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.search.PsiElementProcessor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import me.mattco.serenityos.gml.psi.api.GMLComponent
import me.mattco.serenityos.gml.psi.api.GMLComponentBody
import me.mattco.serenityos.gml.psi.api.GMLVisitor

class GMLBlockFoldingBuilder : CustomFoldingBuilder() {
    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean
    ) {
        val visitor = FoldingVisitor(descriptors)
        PsiTreeUtil.processElements(root) {
            it.accept(visitor)
            true
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange) = "{ ... }"

    override fun isRegionCollapsedByDefault(p0: ASTNode) = false

    private class FoldingVisitor(private val descriptors: MutableList<FoldingDescriptor>) : GMLVisitor() {
        override fun visitComponent(o: GMLComponent) {
            super.visitComponent(o)
            descriptors += FoldingDescriptor(o, TextRange(o.openCurly.startOffset, o.closeCurly.endOffset))
        }
    }
}
