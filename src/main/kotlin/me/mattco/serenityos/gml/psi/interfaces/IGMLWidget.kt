package me.mattco.serenityos.gml.psi.interfaces

import com.intellij.psi.PsiElement
import me.mattco.serenityos.gml.Widget

interface IGMLWidget : PsiElement {
    val gmlWidget: Widget?

    val identWithoutAt: String
}
