package me.mattco.serenityos.gml

import com.intellij.lang.Commenter

class GMLCommenter : Commenter {
    override fun getLineCommentPrefix() = "// "

    override fun getBlockCommentPrefix() = null

    override fun getBlockCommentSuffix() = null

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null
}
