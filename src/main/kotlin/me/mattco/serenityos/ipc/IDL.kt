package me.mattco.serenityos.ipc

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.FileViewProvider

object IPCLanguage : Language("SerenityOS IPC") {
    // TODO: Get an icon for this
    val FILE_ICON = IconLoader.getIcon("/META-INF/pluginIcon.svg", IPCLanguage::class.java)
}

object IPCFileType : LanguageFileType(IPCLanguage) {
    override fun getName() = "SerenityOS IPC"

    override fun getDescription() = "The SerenityOS IPC file type"

    override fun getDefaultExtension() = "ipc"

    override fun getIcon() = IPCLanguage.FILE_ICON
}

class IPCFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, IPCLanguage) {
    override fun getFileType() = IPCFileType
}
