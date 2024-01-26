package me.mattco.serenityos.gml.formatting

import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import me.mattco.serenityos.common.formatting.SpacingBuilder
import me.mattco.serenityos.common.formatting.elementType
import me.mattco.serenityos.gml.GMLFileStub
import me.mattco.serenityos.gml.GMLTypes.*

fun buildGMLSpacingRules(settings: CommonCodeStyleSettings) = SpacingBuilder(settings).apply {
    simple {
        after(AT).spaces(0)
        before(OPEN_CURLY).spaces(1)
        after(OPEN_CURLY).spaces(0)
        after(CLOSE_CURLY).spaces(0)
        before(COLON).spaces(0)
        after(COLON).spaces(1)
        around(PROPERTY_IDENTIFIER).spaces(0)
        before(COMMA).spaces(0)
        after(COMMA).spaces(1)

        for (type in listOf(OPEN_BRACKET, CLOSE_BRACKET, BOOLEAN, STRING, NUMBER, NAMESPACE))
            around(type).spaces(0)

        between(COLON, COMPONENT).spacing(1, 1, 0, false, 0)
    }

    contextual(parent = COMPONENT_BODY, right = COMPONENT) { _, left, _ ->
        val leftType = left?.elementType ?: return@contextual null
        if (leftType == COLON)
            return@contextual null

        makeSpacing(minLineFeeds = 2, keepBlankLines = 1)
    }
}

fun findAlignmentForGMLNode(node: ASTNode): Alignment? {
    val parentType = node.treeParent?.elementType ?: return null
    if (parentType == COMPONENT_BODY)
        return Alignment.createAlignment()
    return null
}

fun findIndentForGMLNode(node: ASTNode): Indent? {
    if (node.elementType == PROPERTY)
        return Indent.getNormalIndent()

    if (node.elementType == COMPONENT) {
        // Only indent if this isn't a property value
        val parent = node.treeParent ?: return Indent.getNoneIndent()
        val parentType = parent.elementType
        if (parentType != PROPERTY && parentType != GMLFileStub.Type)
            return Indent.getNormalIndent()
    }

    return Indent.getNoneIndent()
}
