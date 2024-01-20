package me.mattco.serenityos.ipc

import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import me.mattco.serenityos.common.DSLAnnotator
import me.mattco.serenityos.idl.findChildrenOfType
import me.mattco.serenityos.ipc.psi.api.IPCAttributeList
import me.mattco.serenityos.ipc.psi.api.IPCEndpoint
import me.mattco.serenityos.ipc.psi.api.IPCEndpointFunction
import me.mattco.serenityos.ipc.psi.api.IPCInclude
import me.mattco.serenityos.ipc.psi.api.IPCIncludePath
import me.mattco.serenityos.ipc.psi.api.IPCType

class IPCSyntaxAnnotator : DSLAnnotator(), DumbAware {
    override fun annotate(element: PsiElement, holder: Holder) = with(holder) {
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
