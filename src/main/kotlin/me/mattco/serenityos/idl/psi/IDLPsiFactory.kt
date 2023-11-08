package me.mattco.serenityos.idl.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import me.mattco.serenityos.idl.descendantOfType
import me.mattco.serenityos.idl.IDLFile
import me.mattco.serenityos.idl.IDLFileType
import me.mattco.serenityos.idl.psi.api.IDLNamespace

class IDLPsiFactory(private val project: Project) {
    fun createFile(text: String, fileName: String = "dummy.idl") = PsiFileFactory
        .getInstance(project)
        .createFileFromText(
            fileName,
            IDLFileType,
            text,
        ) as IDLFile

    fun createIdentifier(name: String) = createFromText<IDLNamespace>("namespace $name {}")?.identifier
        ?: error("Failed to create namespace")

    inline fun <reified T : PsiElement> createFromText(text: String): T? = createFile(text).descendantOfType()
}
