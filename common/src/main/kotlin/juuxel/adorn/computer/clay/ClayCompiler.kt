package juuxel.adorn.computer.clay

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import juuxel.adorn.util.Either
import juuxel.adorn.util.Left
import juuxel.adorn.util.Right

class ClayCompiler {
    private val functions: MutableList<ClayFile.Function> = ArrayList()
    private val errors: MutableList<CompilationError> = ArrayList()
    private val instructions: MutableList<Instruction> = ArrayList()
    private var inFunction = false
    private lateinit var functionName: String
    private lateinit var functionParams: List<String>

    fun compile(file: List<String>): Either<List<CompilationError>, ClayFile> {
        for ((i, line) in file.withIndex()) {
            try {
                readLine(i, line)
            } catch (e: CommandSyntaxException) {
                errors += CompilationError(i, line, e.cursor, e.rawMessage.string)
            }
        }

        return if (errors.isNotEmpty()) {
            Right(ClayFile(functions))
        } else {
            Left(errors)
        }
    }

    private fun readLine(index: Int, line: String) {
        val reader = StringReader(line)
        reader.skipWhitespace()
        if (reader.peek() == COMMENT_START) return

        if (inFunction) {
            readFunctionBody(index, line, reader)
        } else {
            if (reader.readIdentifier() != FUNCTION_START) {
                errors += CompilationError(index, line, reader.cursor, "Expected function")
            }
            reader.skipWhitespace()
            val name = reader.readIdentifier()
            reader.expect('(')
            val parameters = ArrayList<String>()
            while (reader.canRead() && reader.peek() != ')') {
                while (reader.canRead() && reader.peek().let { it == ',' || it.isWhitespace() }) {
                    reader.skip()
                }
                parameters += reader.readIdentifier()
            }
            reader.expect(')')
            functionName = name
            functionParams = parameters
            inFunction = true
            instructions.clear()
        }

        reader.skipWhitespace()
        if (reader.canRead()) {
            if (reader.peek() != COMMENT_START) {
                errors += CompilationError(index, line, reader.cursor, "Unexpected text")
            }
        }
    }

    private fun readFunctionBody(index: Int, line: String, reader: StringReader) {
        val rawCmd = reader.readIdentifier()
        val cmd = rawCmd.lowercase()

        if (cmd == FUNCTION_END) {
            inFunction = false
            functions += ClayFile.Function(functionName, functionParams, ArrayList(instructions))
            return
        }

        val insn: Instruction = when (cmd) {
            "dup" -> Instruction.Simple.DUP
            "pop" -> Instruction.Simple.POP
            "calldyn" -> Instruction.Simple.CALL_DYNAMIC

            "add" -> Instruction.Simple.ADD
            "sub" -> Instruction.Simple.SUBTRACT
            "mul" -> Instruction.Simple.MULTIPLY
            "div" -> Instruction.Simple.DIVIDE
            "rem" -> Instruction.Simple.REMAINDER
            "pow" -> Instruction.Simple.POWER

            "equal" -> Instruction.Simple.EQUAL
            "less" -> Instruction.Simple.LESS_THAN
            "lesseq" -> Instruction.Simple.LESS_THAN_EQUAL
            "greater" -> Instruction.Simple.GREATER_THAN
            "greatereq" -> Instruction.Simple.GREATER_THAN_EQUAL
            "or" -> Instruction.Simple.OR
            "and" -> Instruction.Simple.AND
            "not" -> Instruction.Simple.NOT
            "return" -> Instruction.Simple.RETURN

            "call" -> {
                reader.skipWhitespace()
                val head = reader.readIdentifier()
                reader.skipWhitespace()
                val desc = if (reader.canRead() && reader.peek() == '.') {
                    val tail = reader.readIdentifier()
                    Descriptor(head, tail)
                } else {
                    Descriptor(null, head)
                }
                Instruction.Call(desc)
            }

            "jump" -> {
                reader.skipWhitespace()
                val label = reader.readQuotedString()
                Instruction.Jump(label)
            }

            "jumpif" -> {
                reader.skipWhitespace()
                val label = reader.readQuotedString()
                Instruction.JumpIf(label)
            }

            "label" -> {
                reader.skipWhitespace()
                val label = reader.readQuotedString()
                Instruction.Label(label)
            }

            "push" -> {
                reader.skipWhitespace()
                if (!reader.canRead()) {
                    errors += CompilationError(index, line, reader.cursor, "Expected value to push")
                    return
                }
                val next = reader.peek()
                if (StringReader.isQuotedStringStart(next)) {
                    Instruction.ConstantString(reader.readQuotedString())
                } else if (StringReader.isAllowedNumber(next)) {
                    val start = reader.cursor
                    try {
                        Instruction.ConstantInt(reader.readInt())
                    } catch (e: CommandSyntaxException) {
                        reader.cursor = start
                        Instruction.ConstantDouble(reader.readDouble())
                    }
                } else {
                    val word = reader.readIdentifier()
                    when (word.lowercase()) {
                        "true" -> Instruction.ConstantBool(true)
                        "false" -> Instruction.ConstantBool(false)
                        else -> {
                            errors += CompilationError(index, line, reader.cursor, "Expected a valid literal, found '$word'")
                            return
                        }
                    }
                }
            }

            "arg" -> {
                reader.skipWhitespace()
                val name = reader.readIdentifier()
                Instruction.Arg(name)
            }

            else -> {
                errors += CompilationError(index, line, reader.cursor, "Unknown command: $rawCmd")
                return
            }
        }

        instructions += insn
    }

    companion object {
        private const val COMMENT_START = '#'
        private const val FUNCTION_START = "fn"
        private const val FUNCTION_END = "end"

        private fun isValidIdentifierChar(c: Char): Boolean =
            c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'

        private fun StringReader.readIdentifier(): String {
            val start = cursor
            while (canRead() && isValidIdentifierChar(peek())) {
                skip()
            }
            return string.substring(start, cursor)
        }
    }

    /**
     * A compilation error.
     * @property lineNum the line number, 0-indexed
     * @property line    the line text
     * @property column  the character on the line, 0-indexed
     * @property error   the error
     */
    data class CompilationError(val lineNum: Int, val line: String, val column: Int, val error: String)
}
