package me.mattco.serenityos.gml

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.FlexLexer
import com.intellij.psi.tree.IElementType

class GMLLexerAdapter : FlexAdapter(GMLLexer(null))

class GMLToken(debugName: String) : IElementType(debugName, GMLLanguage) {
    override fun toString() = "GMLToken.${super.toString()}"
}

class GMLElementType(debugName: String) : IElementType(debugName, GMLLanguage)

abstract class GMLLexerBase : FlexLexer
