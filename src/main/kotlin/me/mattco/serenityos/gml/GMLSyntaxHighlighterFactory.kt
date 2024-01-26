package me.mattco.serenityos.gml

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import me.mattco.serenityos.gml.GMLTypes.*
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

object Highlights {
    val COMMENT = Default.LINE_COMMENT.extend("COMMENT")
    val IDENTIFIER = Default.IDENTIFIER.extend("IDENTIFIER")
    val NUMBER = Default.NUMBER.extend("NUMBER")
    val STRING = Default.STRING.extend("STRING")
    val BOOLEAN = Default.NUMBER.extend("BOOLEAN")

    val BRACES = Default.BRACES.extend("BRACES")
    val BRACKETS = Default.BRACKETS.extend("BRACKETS")
    val COMMA = Default.COMMA.extend("COMMA")
    val OPERATOR = Default.OPERATION_SIGN.extend("OPERATOR")
    val NAMESPACE = OPERATOR.extend("NAMESPACE")
    val AT = OPERATOR.extend("AT")
    val COLON = OPERATOR.extend("COLON")

    val NAMESPACE_NAME = Default.CLASS_NAME.extend("NAMESPACE_NAME")
    val CLASS_NAME = NAMESPACE_NAME.extend("CLASS_NAME")
    val PROPERTY_NAME = Default.INSTANCE_FIELD.extend("PROPERTY_NAME")

    private fun TextAttributesKey.extend(name: String) = TextAttributesKey.createTextAttributesKey("GML_$name", this)
}

class GMLSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = GMLLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            COMMENT -> Highlights.COMMENT
            NUMBER -> Highlights.NUMBER
            STRING -> Highlights.STRING

            OPEN_CURLY, CLOSE_CURLY -> Highlights.BRACES
            OPEN_BRACKET, CLOSE_BRACKET -> Highlights.BRACKETS

            COMMA -> Highlights.COMMA
            NAMESPACE -> Highlights.NAMESPACE
            AT -> Highlights.AT
            COLON -> Highlights.COLON

            // Identifiers are handled in the annotator
            IDENTIFIER -> return emptyArray()

            else -> {
                if (tokenType is GMLToken)
                    println("No token highlighting defined for element $tokenType!")
                return emptyArray()
            }
        }.let { arrayOf(it) }
    }
}

class GMLSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = GMLSyntaxHighlighter()
}
