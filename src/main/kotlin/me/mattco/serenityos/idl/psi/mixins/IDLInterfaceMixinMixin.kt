package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLInterfaceMixin

abstract class IDLInterfaceMixinMixin(node: ASTNode) : IDLNamedElement(node), IDLInterfaceMixin, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
