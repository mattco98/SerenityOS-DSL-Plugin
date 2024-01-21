package me.mattco.serenityos.gml.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import me.mattco.serenityos.ipc.IPCTypes

abstract class GMLNamedElement(node: ASTNode) : ASTWrapperPsiElement(node), GMLNameIdentifierOwner {
    override fun getNameIdentifier(): PsiElement? = findChildByType(IPCTypes.IDENTIFIER)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement = apply {
        nameIdentifier?.replace(GMLPsiFactory(project).createIdentifier(name))
    }

    override fun getTextOffset() = nameIdentifier?.textOffset ?: super.getTextOffset()
}
