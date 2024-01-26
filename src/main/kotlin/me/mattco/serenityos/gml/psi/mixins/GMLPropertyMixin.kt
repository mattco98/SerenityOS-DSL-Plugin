package me.mattco.serenityos.gml.psi.mixins

import com.intellij.lang.ASTNode
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.gml.Property
import me.mattco.serenityos.gml.psi.GMLNamedElement
import me.mattco.serenityos.gml.psi.api.GMLComponent
import me.mattco.serenityos.gml.psi.api.GMLProperty

abstract class GMLPropertyMixin(node: ASTNode) : GMLNamedElement(node), GMLProperty {
    override val gmlProperty: Property?
        get() = ancestorOfType<GMLComponent>()?.gmlComponent?.properties?.find {
            it.name == propertyIdentifier.identifier.text
        }
}