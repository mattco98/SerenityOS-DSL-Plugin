package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLTypedef

abstract class IDLTypedefMixin(node: ASTNode) : IDLNamedElement(node), IDLTypedef, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
