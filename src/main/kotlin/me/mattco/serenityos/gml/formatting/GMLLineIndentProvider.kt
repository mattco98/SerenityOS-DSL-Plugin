package me.mattco.serenityos.gml.formatting

import com.intellij.formatting.Indent
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.source.codeStyle.SemanticEditorPosition.SyntaxElement
import com.intellij.psi.impl.source.codeStyle.lineIndent.IndentCalculator
import com.intellij.psi.impl.source.codeStyle.lineIndent.JavaLikeLangLineIndentProvider
import com.intellij.psi.tree.IElementType
import me.mattco.serenityos.gml.GMLLanguage
import me.mattco.serenityos.gml.GMLTypes.*

class GMLLineIndentProvider : JavaLikeLangLineIndentProvider() {
    override fun mapType(tokenType: IElementType) = when (tokenType) {
        OPEN_CURLY -> GMLSyntaxElement.OpenCurly
        CLOSE_CURLY -> GMLSyntaxElement.CloseCurly
        CLOSE_BRACKET, NUMBER, STRING, BOOLEAN -> GMLSyntaxElement.Value
        else -> null
    }

    override fun getIndent(project: Project, editor: Editor, language: Language?, offset: Int): IndentCalculator? {
        if (getPosition(editor, offset).matchesRule { it.isAfterOnSameLine(GMLSyntaxElement.Value) }) {
            return IndentCalculatorFactory(project, editor).createIndentCalculator(
                Indent.Type.NONE,
                IndentCalculator.LINE_BEFORE
            )
        } else if (getPosition(editor, offset).matchesRule { it.isAfterOnSameLine(GMLSyntaxElement.OpenCurly) }
            && !getPosition(editor, offset).matchesRule { it.isAfterOnSameLine(GMLSyntaxElement.CloseCurly) }) {
            return IndentCalculatorFactory(project, editor).createIndentCalculator(
                Indent.Type.NORMAL,
                IndentCalculator.LINE_BEFORE
            )
        }

        return IndentCalculatorFactory(project, editor).createIndentCalculator(
            Indent.Type.NONE,
            IndentCalculator.LINE_AFTER
        )
    }

    override fun isSuitableForLanguage(language: Language) = language == GMLLanguage

    enum class GMLSyntaxElement : SyntaxElement {
        OpenCurly,
        CloseCurly,
        Value,
    }
}
