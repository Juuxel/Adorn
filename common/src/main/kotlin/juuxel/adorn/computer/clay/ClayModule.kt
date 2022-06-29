package juuxel.adorn.computer.clay

sealed interface ClayModule {
    val functions: List<Function>

    sealed interface Function {
        val fnName: String
        val arity: Int

        data class OfFile(val function: ClayFile.Function) : Function {
            override val fnName = function.name
            override val arity = function.parameters.size
        }

        enum class Builtin(override val fnName: String, override val arity: Int) : Function {
            IO_READ("read", 0),
            IO_PRINT("print", 1),
            IO_PRINTLN("println", 1),
        }
    }

    data class OfFile(val file: ClayFile) : ClayModule {
        override val functions = file.functions.map { Function.OfFile(it) }
        enum class Builtin(vararg functions: Function) : ClayModule {
            IO(
                Function.Builtin.IO_READ,
                Function.Builtin.IO_PRINT,
                Function.Builtin.IO_PRINTLN,
            ),
            ;
            override val functions: List<Function> = listOf(*functions)
        }
    }
}
