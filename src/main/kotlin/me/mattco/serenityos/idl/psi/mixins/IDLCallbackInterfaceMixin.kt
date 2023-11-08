package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLCallbackInterface
import me.mattco.serenityos.idl.psi.singleRef

abstract class IDLCallbackInterfaceMixin(node: ASTNode) : IDLNamedElement(node), IDLCallbackInterface, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
