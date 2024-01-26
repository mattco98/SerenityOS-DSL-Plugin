package me.mattco.serenityos.gml.formatting

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import me.mattco.serenityos.gml.GMLFileStub
import me.mattco.serenityos.gml.GMLLexerAdapter
import me.mattco.serenityos.gml.GMLLexerBase
import me.mattco.serenityos.gml.GMLTypes

class GMLBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?)
        = contextType in ALLOWED_CONTEXT_TOKENS

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset

    companion object {
        val PAIRS = arrayOf(
            BracePair(GMLTypes.OPEN_CURLY, GMLTypes.CLOSE_CURLY, true),
            BracePair(GMLTypes.OPEN_BRACKET, GMLTypes.CLOSE_BRACKET, false),
        )

        private val ALLOWED_CONTEXT_TOKENS = setOf(
            GMLTypes.CLOSE_CURLY,
            GMLTypes.COMMENT,
            GMLLexerBase.SPACE,
        )
    }
}
