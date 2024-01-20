package me.mattco.serenityos.ipc.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface IPCNameIdentifierOwner : IPCPsiElement, PsiNameIdentifierOwner, NavigatablePsiElement
