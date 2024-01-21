package me.mattco.serenityos.common

import com.intellij.lang.Commenter

class DSLCommenter : Commenter {
    override fun getLineCommentPrefix() = "// "

    override fun getBlockCommentPrefix() = null

    override fun getBlockCommentSuffix() = null

    override fun getCommentedBlockCommentPrefix() = null

    override fun getCommentedBlockCommentSuffix() = null
}
