package juuxel.adorn.computer.clay

import juuxel.adorn.util.DupDeque
import java.util.ArrayDeque
import java.util.Deque
import java.util.TreeMap

class ClayInterpreter(private val bridge: Bridge) {
    private val callStack: Deque<Descriptor> = ArrayDeque()
    private val stack: DupDeque<Deque<Value>> = DupDeque { ArrayDeque(it) }
    private val modules: MutableMap<String, ClayModule> = TreeMap()

    fun addModule(name: String, module: ClayModule) {
        modules[name] = module
    }

    fun pushMain(module: String) =
        push(Descriptor(module, "main"))

    private fun error(instruction: Int, message: String) =
        bridge.error(ExecutionError(callStack.toList(), instruction, message))

    fun push(function: Descriptor) {
        val callee = callStack.peek()
        callStack.push(function)
        if (callStack.size > MAX_CALL_STACK_SIZE) {
            return error(0, "Call stack overflow")
        }

        val moduleName = function.module ?: callee?.module
        val module = modules[moduleName] ?: return error(0, "Unknown module: $moduleName")
        val fn = module.functions.find {
            it.fnName == function.function
        } ?: return error(0, "Unknown function: ${function.function}")

        stack.duplicate()
        stack.pop()
        callStack.pop()
    }

    companion object {
        private const val MAX_CALL_STACK_SIZE = 100
        private const val MAX_VALUE_STACK_SIZE = 100
    }

    interface Bridge {
        fun read(): String?
        fun print(str: String)
        fun error(error: ExecutionError)
    }

    /**
     * An error in executing a function.
     *
     * @param callStack   the call stack, most recent function first
     * @param instruction the instruction (1-indexed), or 0 if it happened when verifying the function
     * @param message     the error message
     */
    data class ExecutionError(val callStack: List<Descriptor>, val instruction: Int, val message: String)

    sealed class Value(val typeName: String) {
        data class OfString(val value: String) : Value("string")
        data class OfInt(val value: Int) : Value("int")
        data class OfDouble(val value: Double) : Value("real")
        data class OfBool(val value: Boolean) : Value("bool")
        data class OfList(val value: List<Value>) : Value("list")
        data class OfMap(val value: Map<String, Value>) : Value("map")
    }
}
