package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLInterface

abstract class IDLInterfaceMixin(node: ASTNode) : IDLNamedElement(node), IDLInterface, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
