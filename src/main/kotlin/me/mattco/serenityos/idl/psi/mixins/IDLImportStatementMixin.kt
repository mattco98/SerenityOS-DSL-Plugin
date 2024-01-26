package me.mattco.serenityos.idl.psi.mixins

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import me.mattco.serenityos.idl.IDLFile
import me.mattco.serenityos.idl.project.idlProject
import me.mattco.serenityos.idl.psi.IDLNamedElement
import me.mattco.serenityos.idl.psi.api.IDLImportStatement
import me.mattco.serenityos.idl.psi.singleRef
import kotlin.io.path.Path

abstract class IDLImportStatementMixin(node: ASTNode) : IDLNamedElement(node), IDLImportStatement {
    override fun getReference(): PsiReference? = singleRef(IDLImportStatement::resolveFile)

    override fun getNameIdentifier() = importPath
}

fun IDLImportStatement.resolveFile(): IDLFile? =
    idlProject.resolveImportedFile(containingFile.originalFile.virtualFile, Path(importPath.text.drop(1).dropLast(1)))
