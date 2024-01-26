package me.mattco.serenityos.idl

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import me.mattco.serenityos.idl.psi.IDLDeclaration
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.*

class IDLFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        IDLLexerAdapter(),
        TokenSet.create(IDLTypes.IDENT),
        TokenSet.create(IDLTypes.COMMENT),
        TokenSet.create(IDLTypes.STRING),
    )

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        return element.elementType == IDLTypes.IDENTIFIER && element.parent is IDLDeclaration
    }

    override fun getHelpId(element: PsiElement) = HelpID.FIND_OTHER_USAGES

    override fun getType(element: PsiElement): String {
        return when (element) {
            is IDLCallback -> "callback"
            is IDLCallbackInterface -> "callback interface"
            is IDLDictionary -> "dictionary"
            is IDLEnum -> "enum"
            is IDLInterface -> "interface"
            is IDLInterfaceMixin -> "interface mixin"
            else -> "TODO: IDLFindUsagesProvider::getType for ${element::class.simpleName}"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return if (element is IDLNamedElement) {
            element.name ?: "ERROR"
        } else "TODO: IDLFindUsagesProvider::getDescriptiveName for ${element::class.simpleName}"
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return "TODO: IDLFindUsagesProvider::getNodeText"
    }
}
