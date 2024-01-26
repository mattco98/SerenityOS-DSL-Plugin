package me.mattco.serenityos.gml

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.FileViewProvider

object GMLLanguage : Language("SerenityOS GML") {
    val FILE_ICON = IconLoader.getIcon("/META-INF/gml.png", GMLLanguage::class.java)
}

object GMLFileType : LanguageFileType(GMLLanguage) {
    override fun getName() = "SerenityOS GML"

    override fun getDescription() = "The SerenityOS GML file type"

    override fun getDefaultExtension() = "gml"

    override fun getIcon() = GMLLanguage.FILE_ICON
}

class GMLFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GMLLanguage) {
    override fun getFileType() = GMLFileType
}
