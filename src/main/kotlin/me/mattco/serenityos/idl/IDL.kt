package me.mattco.serenityos.idl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.FileViewProvider

object IDLLanguage : com.intellij.lang.Language("SerenityOS IDL") {
    val FILE_ICON = IconLoader.getIcon("/META-INF/pluginIcon.svg", IDLLanguage::class.java)
}

object IDLFileType : LanguageFileType(IDLLanguage) {
    override fun getName() = "SerenityOS IDL"

    override fun getDescription() = "The SerenityOS IDL file type"

    override fun getDefaultExtension() = "idl"

    override fun getIcon() = IDLLanguage.FILE_ICON
}

class IDLFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, IDLLanguage) {
    override fun getFileType() = IDLFileType
}

