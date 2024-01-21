package me.mattco.serenityos.gml.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface GMLNameIdentifierOwner : GMLPsiElement, PsiNameIdentifierOwner, NavigatablePsiElement
