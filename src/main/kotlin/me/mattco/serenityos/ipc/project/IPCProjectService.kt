package me.mattco.serenityos.ipc.project

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.cidr.lang.psi.OCFile
import java.nio.file.Path

interface IPCProjectService {
    fun resolveImportedFile(path: Path): OCFile?
}

val Project.ipcProject: IPCProjectService
    get() = service()

val PsiElement.ipcProject: IPCProjectService
    get() = project.ipcProject
