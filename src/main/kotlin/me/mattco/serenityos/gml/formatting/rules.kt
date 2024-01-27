package me.mattco.serenityos.gml.formatting

import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import me.mattco.serenityos.common.formatting.SpacingBuilder
import me.mattco.serenityos.common.formatting.elementType
import me.mattco.serenityos.gml.GMLTypes.*

fun buildGMLSpacingRules(settings: CommonCodeStyleSettings) = SpacingBuilder(settings).apply {
    simple {
        between(OPEN_CURLY, CLOSE_CURLY).spacing(0, 0, 0, false, 0)
        between(PROPERTY, CLOSE_CURLY).spacing(0, 0, 0, false, 0)
        between(OPEN_CURLY, COMPONENT_BODY).spacing(0, 0, 1, false, 0)
        between(COMPONENT_BODY, CLOSE_CURLY).spacing(0, 0, 1, false, 0)

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
        between(PROPERTY, PROPERTY).spacing(0, 0, 1, false, 0)
        between(PROPERTY, COMPONENT).spacing(0, 0, 2, false, 0)
        between(COMPONENT, PROPERTY).spacing(0, 0, 2, false, 0)
    }

    contextual(parent = COMPONENT_BODY, right = COMPONENT) { _, left, _ ->
        val leftType = left?.elementType ?: return@contextual null
        if (leftType == COLON)
            return@contextual null

        makeSpacing(minLineFeeds = 2, keepBlankLines = 1)
    }
}

fun findIndentForGMLNode(node: ASTNode): Indent? {
    if (node.elementType == COMPONENT_BODY)
        return Indent.getNormalIndent()

    return Indent.getNoneIndent()
}
