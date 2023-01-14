package juuxel.adorn.datagen.io

class JsonOutput {
    private var indentLevel = 0
    private val builder = StringBuilder()

    private fun nextLine() {
        builder.append('\n').append(INDENT_STRING.repeat(indentLevel))
    }

    fun beginObject() {
        builder.append('{')
        indentLevel++
        nextLine()
    }

    private fun deleteComma() {
        for (i in builder.lastIndex downTo 0) {
            val c = builder[i]
            if (!c.isWhitespace()) {
                if (c == ',') {
                    builder.deleteCharAt(i)
                }

                break
            }
        }
    }

    private fun unindent() {
        builder.delete(builder.length - INDENT_STRING.length, builder.length)
    }

    fun endObject() {
        deleteComma()
        unindent()
        builder.append('}')
        indentLevel--
    }

    fun beginArray() {
        builder.append('[')
        indentLevel++
        nextLine()
    }

    fun endArray() {
        deleteComma()
        unindent()
        builder.append(']')
        indentLevel--
    }

    fun endCollectionKey() {
        builder.append(": ")
    }

    fun endCollectionValue() {
        builder.append(',')
        nextLine()
    }

    fun primitive(value: Any?) {
        when (value) {
            null -> builder.append("null")
            is Boolean, is Long, is Int, is Short, is Byte -> builder.append(value)
            is Number -> builder.append(value.toDouble())
            else -> builder.append('"').append(escape(value.toString())).append('"')
        }
    }

    private fun escape(str: String): String {
        val chars = str.flatMap {
            if (it in SAFE_CHARACTERS) {
                listOf(it)
            } else {
                val code = it.code.toString(radix = 16).padStart(4, padChar = '0')
                "\\u$code".toCharArray().asIterable()
            }
        }
        return String(chars.toCharArray())
    }

    fun print(value: Any?) {
        when (value) {
            is Collection<*> -> {
                beginArray()
                for (v in value) {
                    print(v)
                    endCollectionValue()
                }
                endArray()
            }

            is Map<*, *> -> {
                beginObject()
                for ((k, v) in value) {
                    primitive(k.toString())
                    endCollectionKey()
                    print(v)
                    endCollectionValue()
                }
                endObject()
            }

            else -> primitive(value)
        }
    }

    override fun toString(): String =
        builder.toString()

    companion object {
        private const val INDENT_STRING = "  "
        private const val SAFE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-*,'/&%#.?!:;()[]{}_~^@=\$|<>"
    }
}
