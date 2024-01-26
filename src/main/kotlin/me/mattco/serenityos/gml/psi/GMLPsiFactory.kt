package me.mattco.serenityos.gml.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import me.mattco.serenityos.common.descendantOfType
import me.mattco.serenityos.common.findChildOfType
import me.mattco.serenityos.gml.GMLFile
import me.mattco.serenityos.gml.GMLFileType
import me.mattco.serenityos.gml.GMLTypes
import me.mattco.serenityos.gml.psi.api.GMLComponent

class GMLPsiFactory(private val project: Project) {
    fun createFile(text: String, fileName: String = "dummy.gml") = PsiFileFactory
        .getInstance(project)
        .createFileFromText(
            fileName,
            GMLFileType,
            text
        ) as GMLFile

    fun createIdentifier(name: String) = createFromText<GMLComponent>("@$name {}")
        ?.componentName
        ?.findChildOfType(GMLTypes.IDENTIFIER)
        ?: error("Failed to create endpoint")

    inline fun <reified T : PsiElement> createFromText(text: String): T? = createFile(text).descendantOfType()
}