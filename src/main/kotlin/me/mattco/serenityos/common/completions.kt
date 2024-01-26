package me.mattco.serenityos.common

import com.intellij.openapi.util.Key
import com.intellij.patterns.*
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.util.ProcessingContext

typealias PsiPattern = ElementPattern<PsiElement>

fun psiElement(type: IElementType) = PlatformPatterns.psiElement(type)

inline fun <reified T : PsiElement> psiElement(): PsiElementPattern.Capture<T> =
    PlatformPatterns.psiElement(T::class.java)

inline fun <T : Any, Self> ObjectPattern<T, Self>.condition(
    crossinline block: (element: T, context: ProcessingContext) -> Boolean,
): Self
    where Self : ObjectPattern<T, Self>
{
    return with(object : PatternCondition<T>("condition") {
        override fun accepts(t: T, context: ProcessingContext?) =
            if (context == null) false else block(t, context)
    })
}

inline fun <T : Any, Self> ObjectPattern<T, Self>.debug(
    crossinline block: (element: T, context: ProcessingContext?) -> Unit,
): Self
    where Self : ObjectPattern<T, Self>
{
    return with(object : PatternCondition<T>("debug") {
        override fun accepts(t: T, context: ProcessingContext?): Boolean {
            block(t, context)
            return true
        }
    })
}

inline operator fun <reified T> ProcessingContext.set(key: Key<T>, value: T) = this.put(key, value)
