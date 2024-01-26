package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLEnum

abstract class IDLEnumMixin(node: ASTNode) : IDLNamedElement(node), IDLEnum, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
