package me.mattco.serenityos.gml.psi.interfaces

import com.intellij.psi.PsiElement
import me.mattco.serenityos.gml.Property

interface IGMLProperty : PsiElement {
    val gmlProperty: Property?
}
