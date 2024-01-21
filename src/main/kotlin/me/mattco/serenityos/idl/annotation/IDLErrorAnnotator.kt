package me.mattco.serenityos.idl.annotation

import ai.grazie.utils.dropPrefix
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.common.prevSiblings
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.api.IDLCallbackInterface
import me.mattco.serenityos.idl.psi.api.IDLExtendedAttributeList
import me.mattco.serenityos.idl.psi.api.IDLIdent
import me.mattco.serenityos.idl.psi.api.IDLInterface
import me.mattco.serenityos.idl.psi.api.IDLInterfaceMixin
import java.net.MalformedURLException
import java.net.URL

class IDLErrorAnnotator : DSLAnnotator() {
    override fun annotate(element: PsiElement, holder: Holder) = with(holder) {
        if (element is IDLIdent) {
            if (element.reference != null && element.reference?.resolve() == null) {
                element.highlightError("Unknown type ${element.text}")
                return
            }
        }

        if (element !is IDLInterfaceMixin && element !is IDLInterface && element !is IDLCallbackInterface)
            return

        // Warn for interfaces that don't have a spec link
        val comments = element.parent
            .prevSiblings()
            .takeWhile { it is PsiWhiteSpace || it is PsiComment || it is IDLExtendedAttributeList }
            .filterIsInstance<PsiComment>()
            .map { it.text }

        for (comment in comments) {
            try {
                URL(comment.dropPrefix("//").trim())
                return
            } catch (e: MalformedURLException) {
                continue
            }
        }

        val target = (element as? IDLDeclaration)?.nameIdentifier ?: element
        target.highlightError("This declaration has no spec link")
    }
}
