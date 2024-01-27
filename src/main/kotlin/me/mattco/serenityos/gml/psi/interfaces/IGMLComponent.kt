package me.mattco.serenityos.gml.psi.interfaces

import com.intellij.psi.PsiElement
import me.mattco.serenityos.gml.Component

interface IGMLComponent : PsiElement {
    val gmlComponent: Component?

    val identWithoutAt: String
}
