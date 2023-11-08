package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLDictionary
import me.mattco.serenityos.idl.psi.singleRef

abstract class IDLDictionaryMixin(node: ASTNode) : IDLNamedElement(node), IDLDictionary, IDLDeclaration {
    override fun getNameIdentifier() = identifier
}
