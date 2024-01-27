package me.mattco.serenityos.gml

import ai.grazie.utils.dropPrefix
import com.intellij.codeInsight.documentation.DocumentationManagerUtil
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.DocumentationSettings
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.jetbrains.cidr.lang.psi.OCFile
import com.jetbrains.cidr.lang.psi.OCStructLike
import me.mattco.serenityos.common.ancestorOfType
import me.mattco.serenityos.gml.psi.api.GMLWidget
import me.mattco.serenityos.gml.psi.api.GMLWidgetName
import me.mattco.serenityos.gml.psi.api.GMLProperty
import me.mattco.serenityos.gml.psi.api.GMLPropertyIdentifier

class GMLDocumentationProvider : AbstractDocumentationProvider() {
    override fun getDocumentationElementForLink(
        psiManager: PsiManager,
        link: String,
        context: PsiElement
    ): PsiElement? {
        val name = link.substringAfterLast('/')
        val scope = GlobalSearchScope.projectScope(psiManager.project)
        val file = FilenameIndex.getVirtualFilesByName(name, scope).find {
            it.path == link
        }?.let(psiManager::findFile) ?: return null
        return PsiTreeUtil.findChildrenOfType(file, OCStructLike::class.java).find {
            it.name == name.substringBefore('.')
        }
    }

    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        if (contextElement?.elementType == GMLTypes.IDENTIFIER) {
            val propIdent = contextElement?.ancestorOfType<GMLPropertyIdentifier>()
            if (propIdent != null)
                return propIdent.ancestorOfType<GMLProperty>()

            val compIdent = contextElement?.ancestorOfType<GMLWidgetName>()
            if (compIdent != null)
                return compIdent.ancestorOfType<GMLWidget>()

            return null
        }

        // Also include the '@' for widget names
        if (contextElement?.elementType == GMLTypes.AT)
            return contextElement?.ancestorOfType<GMLWidget>()

        return super.getCustomDocumentationElement(editor, file, contextElement, targetOffset)
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return when (element) {
            is GMLWidget -> {
                val widget = element.gmlWidget ?: return null
                buildString {
                    append(DocumentationMarkup.DEFINITION_START)
                    appendStyled(widget.name, Highlights.WIDGET_NAME)
                    if (widget.inherits != null) {
                        append(" : ")
                        appendStyled(widget.inherits, Highlights.WIDGET_NAME)
                    }
                    append(DocumentationMarkup.DEFINITION_END)

                    if (widget.description != null) {
                        append(DocumentationMarkup.CONTENT_START)
                        append("<p>${widget.description}</p>")
                        append(DocumentationMarkup.CONTENT_END)
                    }

                    val definingStruct = element.reference?.resolve() as? OCStructLike ?: return@buildString
                    val containingFile = definingStruct.containingFile as? OCFile ?: return@buildString
                    val path = containingFile.virtualFile.path
                    val relativePath = path.substringAfter("Userland/").dropPrefix("Libraries/")
                    if (relativePath.isEmpty())
                        return@buildString

                    val link = buildString {
                        DocumentationManagerUtil.createHyperlink(this, definingStruct, path, relativePath, false, false)
                    }
                    append(DocumentationMarkup.CONTENT_START)
                    append("<p>Defined in ${link}</p>")
                    append(DocumentationMarkup.CONTENT_END)
                }
            }
            is GMLProperty -> {
                val property = element.gmlProperty ?: return null
                buildString {
                    append(DocumentationMarkup.DEFINITION_START)
                    appendStyled(property.name, Highlights.PROPERTY_NAME)
                    appendStyled(": ", Highlights.COLON)
                    append(property.type.presentation())
                    append(DocumentationMarkup.DEFINITION_END)

                    if (property.description != null) {
                        append(DocumentationMarkup.CONTENT_START)
                        append("<p>${property.description}</p>")
                        append(DocumentationMarkup.CONTENT_END)
                    }
                }
            }
            else -> null
        }
    }

    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement?) =
        generateDoc(element, originalElement)

    private fun StringBuilder.appendStyled(value: String, key: TextAttributesKey) {
        @Suppress("UnstableApiUsage")
        HtmlSyntaxInfoUtil.appendStyledSpan(
            this,
            key,
            value,
            DocumentationSettings.getHighlightingSaturation(false),
        )
    }
}
