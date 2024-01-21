package me.mattco.serenityos.gml

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.tree.IStubFileElementType

class GMLFileStub(file: GMLFile?) : PsiFileStubImpl<GMLFile>(file) {
    object Type : IStubFileElementType<GMLFileStub>("GMLFile", GMLLanguage) {
        // Should be incremented when lexer, parser, or stub tree changes
        override fun getStubVersion() = 1
    }
}
