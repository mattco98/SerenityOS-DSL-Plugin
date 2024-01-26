package me.mattco.serenityos.gml

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

class GMLParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?) = GMLLexerAdapter()
    override fun createParser(project: Project?) = GMLParser()

    override fun getFileNodeType() = GMLFileStub.Type

    override fun createElement(node: ASTNode?): PsiElement = GMLTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider) = GMLFile(viewProvider)

    override fun getWhitespaceTokens() = WHITE_SPACES
    override fun getCommentTokens() = COMMENTS
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    companion object {
        private val WHITE_SPACES = TokenSet.create(GMLLexerBase.SPACE)
        private val COMMENTS = TokenSet.create(GMLTypes.COMMENT)
    }
}
