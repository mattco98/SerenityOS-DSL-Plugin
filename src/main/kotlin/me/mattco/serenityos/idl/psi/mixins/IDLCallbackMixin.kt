package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLCallback

abstract class IDLCallbackMixin(node: ASTNode) : IDLNamedElement(node), IDLCallback, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
