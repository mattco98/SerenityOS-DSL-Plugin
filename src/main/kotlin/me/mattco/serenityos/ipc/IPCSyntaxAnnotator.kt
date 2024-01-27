package me.mattco.serenityos.ipc

import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.common.findChildrenOfType
import me.mattco.serenityos.ipc.psi.api.*

class IPCSyntaxAnnotator : DSLAnnotator(), DumbAware {
    override fun annotate(element: PsiElement) {
        when (element) {
            is IPCEndpoint -> element.identifier.highlight(Highlights.ENDPOINT_NAME)
            is IPCEndpointFunction -> {
                element.identifier.highlight(Highlights.FUNCTION_NAME)
                element.parameterList.parameterList.forEach {
                    it.identifier.highlight(Highlights.PARAMETER_NAME)
                }
            }
            is IPCType -> {
                element.typePartList.dropLast(1).forEach {
                    it.highlight(Highlights.NAMESPACE_NAME)
                }
                val typeName = element.typePartList.lastOrNull()
                if (typeName != null) {
                    if (element.typePartList.size == 1 && typeName.text in primitiveTypes) {
                        typeName.identifier.highlight(Highlights.PRIMITIVE_TYPE)
                    } else typeName.identifier.highlight(Highlights.CLASS_TYPE)
                }
            }
            is IPCAttributeList -> element.findChildrenOfType(IPCTypes.IDENTIFIER).forEach {
                it.highlight(Highlights.ATTRIBUTE_NAME)
            }
            is IPCIncludePath -> element.highlight(Highlights.INCLUDE_PATH)
        }
    }

    companion object {
        private val primitiveTypes = setOf(
            "i8", "i16", "i32", "i64", "u8", "u16", "u32", "u64",
            "char", "short", "int", "long", "float", "double", "unsigned"
        )
    }
}
