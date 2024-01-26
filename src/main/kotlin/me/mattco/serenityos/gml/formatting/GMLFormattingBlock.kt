package me.mattco.serenityos.gml.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import me.mattco.serenityos.common.formatting.SpacingBuilder
import me.mattco.serenityos.gml.GMLTypes

class GMLFormattingBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val indent_: Indent?,
    private val spacingBuilder: SpacingBuilder,
) : AbstractBlock(node, wrap, alignment) {
    private val children by lazy {
        val children = generateSequence(myNode.firstChildNode) { it.treeNext }

        children.filter {
            it.elementType !in IGNORED_TYPES
        }.map {
            GMLFormattingBlock(
                it,
                null,
                findAlignmentForGMLNode(it),
                findIndentForGMLNode(it),
                spacingBuilder,
            )
        }.toMutableList()
    }

    override fun getIndent() = indent_

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf() = node.firstChildNode == null

    override fun buildChildren() = children

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        if (node.elementType == GMLTypes.COMPONENT_BODY || node.elementType == GMLTypes.ARRAY)
            return ChildAttributes(Indent.getNormalIndent(), null)
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    companion object {
        private val IGNORED_TYPES = setOf(TokenType.WHITE_SPACE)
    }
}
