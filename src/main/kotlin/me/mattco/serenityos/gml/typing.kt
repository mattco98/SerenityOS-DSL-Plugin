package me.mattco.serenityos.gml

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Component(
    val name: String,
    val header: String,
    val inherits: String? = null,
    val description: String? = null,
    val properties: List<Property> = emptyList(),
)

@Serializable
data class Property(
    val name: String,
    val description: String? = null,
    @SerialName("type")
    val typeString: String,
    @SerialName("min_values")
    val minValues: Int? = null,
    @SerialName("max_values")
    val maxValues: Int? = null,
    val getter: String? = null,
    val setter: String? = null,
) {
    val type by lazy { Type.parse(typeString, minValues, maxValues) }
}

sealed interface Type {
    fun presentation(): kotlin.String

    data object Bool : Type {
        override fun presentation() = "bool"
    }

    data class Int(val signed: Boolean) : Type {
        override fun presentation() = if (signed) "i64" else "u64"
    }

    data object Double : Type {
        override fun presentation() = "double"
    }

    data object String : Type {
        override fun presentation() = "String"
    }

    data object Bitmap : Type {
        override fun presentation() = "Bitmap"
    }

    data object Color : Type {
        override fun presentation() = "Color"
    }

    data object UIDimension : Type {
        override fun presentation() = "UIDimension"
    }

    data object Margins : Type {
        override fun presentation() = "Margins"
    }

    class Array(private val min: kotlin.Int, private val max: kotlin.Int, private val inner: Type) : Type {
        override fun presentation(): kotlin.String = buildString {
            append(inner.presentation().let {
                if (inner is Variant) "($it)" else it
            })
            append('[')
            if (min == max) {
                append(min)
            } else {
                append(min)
                append('-')
                append(max)
            }
            append(']')
        }
    }

    class Variant(val types: List<Type>) : Type {
        override fun presentation() = types.joinToString(" | ") { it.presentation() }
    }

    class EnumType(val name: kotlin.String) : Type {
        override fun presentation() = name
    }

    class ErrorType(val message: kotlin.String) : Type {
        override fun presentation() = "<unknown>"
    }

    companion object {
        // More or less taken from the GML compiler
        fun parse(type: kotlin.String, minValues: kotlin.Int?, maxValues: kotlin.Int?): Type {
            return when {
                type == "String" || type == "ByteString" -> String
                type == "i64" -> Int(true)
                type == "u64" -> Int(false)
                type == "double" -> Double
                type == "bool" -> Bool
                type == "Gfx::Bitmap" -> Bitmap
                type == "Gfx::Color" -> Color
                type == "GUI::UIDimension" -> UIDimension
                type == "GUI::Margins" -> Margins
                type.startsWith("Array") -> {
                    if ('<' !in type || '>' !in type)
                        return ErrorType("Expected template specialization of Array type")

                    val innerType = type.substringAfter('<').substringBeforeLast('>')
                    Array(minValues!!, maxValues!!, parse(innerType, null, null))
                }
                type.startsWith("Variant") -> {
                    if ('<' !in type || '>' !in type)
                        return ErrorType("Expected template specialization of Variant type")

                    val innerType = type.substringAfter('<').substringBeforeLast('>')
                    Variant(innerType.split(',').map {
                        parse(it.trim(), null, null)
                    })
                }
                else -> EnumType(type)
            }
        }
    }
}
