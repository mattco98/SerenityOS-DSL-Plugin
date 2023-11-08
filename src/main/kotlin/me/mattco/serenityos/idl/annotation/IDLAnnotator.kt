package me.mattco.serenityos.idl.annotation

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import me.mattco.serenityos.idl.descendantOfType
import me.mattco.serenityos.idl.psi.IDLDeclaration
import java.util.concurrent.locks.ReentrantLock

abstract class IDLAnnotator : Annotator {
    final override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        annotate(element, Holder(holder))
    }

    protected abstract fun annotate(element: PsiElement, holder: Holder)

    data class Holder(private val holder: AnnotationHolder) {
        fun newAnnotation(severity: HighlightSeverity, message: String? = null) = if (message == null) {
            holder.newSilentAnnotation(severity)
        } else holder.newAnnotation(severity, message)

        fun PsiElement.highlight(attribute: TextAttributesKey) {
            newAnnotation(HighlightSeverity.INFORMATION)
                .range(this)
                .textAttributes(attribute)
                .create()
        }

        fun TextRange.highlight(attribute: TextAttributesKey) {
            newAnnotation(HighlightSeverity.INFORMATION)
                .range(this)
                .textAttributes(attribute)
                .create()
        }

        fun PsiElement.highlightError(message: String) {
            newAnnotation(HighlightSeverity.ERROR, message)
                .range(this)
                .create()
        }
    }
}
