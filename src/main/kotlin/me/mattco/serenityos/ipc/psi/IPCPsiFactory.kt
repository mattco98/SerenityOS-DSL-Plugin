package me.mattco.serenityos.ipc.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import me.mattco.serenityos.idl.descendantOfType
import me.mattco.serenityos.ipc.IPCFile
import me.mattco.serenityos.ipc.IPCFileType
import me.mattco.serenityos.ipc.psi.api.IPCEndpoint

class IPCPsiFactory(private val project: Project) {
    fun createFile(text: String, fileName: String = "dummy.ipc") = PsiFileFactory
        .getInstance(project)
        .createFileFromText(
            fileName,
            IPCFileType,
            text
        ) as IPCFile

    fun createIdentifier(name: String) = createFromText<IPCEndpoint>("endpoint $name {}")?.identifier
        ?: error("Failed to create endpoint")

    inline fun <reified T : PsiElement> createFromText(text: String): T? = createFile(text).descendantOfType()
}
