package juuxel.adorn.computer.clay

import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

sealed interface Instruction {
    val type: InstructionType<*>

    interface InstructionType<I : Instruction> {
        val codec: Codec<I>
    }

    companion object {
        private val TYPES: BiMap<String, InstructionType<*>>

        init {
            val typeBuilder = ImmutableBiMap.builder<String, InstructionType<*>>()
                .put("call", Call.Type)
                .put("jump", Jump.Type)
                .put("jump_if", JumpIf.Type)
                .put("label", Label.Type)
                .put("push_string", ConstantString.Type)
                .put("push_int", ConstantInt.Type)
                .put("push_double", ConstantDouble.Type)
                .put("push_bool", ConstantBool.Type)
                .put("arg", Arg.Type)

            for (simple in Simple.values()) {
                typeBuilder.put(simple.name.lowercase(), simple)
            }

            TYPES = typeBuilder.build()
        }

        val CODEC: Codec<Instruction> =
            Codec.STRING.dispatch(
                { TYPES.inverse()[it.type] },
                { TYPES[it]!!.codec }
            )
    }

    enum class Simple : Instruction, InstructionType<Simple> {
        DUP,          // 1 -> 2
        POP,          // 1 -> 0
        CALL_DYNAMIC, // 1 -> ?

        // Arithmetic
        ADD,       // 2 -> 1
        SUBTRACT,  // 2 -> 1
        MULTIPLY,  // 2 -> 1
        DIVIDE,    // 2 -> 1
        REMAINDER, // 2 -> 1
        POWER,     // 2 -> 1

        // Boolean
        EQUAL,              // 2 -> 1
        LESS_THAN,          // 2 -> 1
        GREATER_THAN,       // 2 -> 1
        LESS_THAN_EQUAL,    // 2 -> 1
        GREATER_THAN_EQUAL, // 2 -> 1
        OR,     // 2 -> 1
        AND,    // 2 -> 1
        NOT,    // 1 -> 1; also for numbers

        // Other
        RETURN, // 1 -> 0
        ;

        override val type = this
        override val codec: Codec<Simple> =
            Codec.unit(this)
    }

    // ? -> ?
    data class Call(val target: Descriptor) : Instruction {
        override val type = Type
        object Type : InstructionType<Call> {
            override val codec: Codec<Call> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Descriptor.CODEC.fieldOf("target").forGetter(Call::target),
                ).apply(instance, ::Call)
            }
        }
    }
    // 0 -> 0
    data class Jump(val label: String) : Instruction {
        override val type = Type
        object Type : InstructionType<Jump> {
            override val codec: Codec<Jump> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("label").forGetter(Jump::label),
                ).apply(instance, ::Jump)
            }
        }
    }
    // 1 -> 0
    data class JumpIf(val label: String) : Instruction {
        override val type = Type
        object Type : InstructionType<JumpIf> {
            override val codec: Codec<JumpIf> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("label").forGetter(JumpIf::label),
                ).apply(instance, ::JumpIf)
            }
        }
    }
    // 0 -> 0
    data class Label(val name: String) : Instruction {
        override val type = Type
        object Type : InstructionType<Label> {
            override val codec: Codec<Label> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Label::name),
                ).apply(instance, ::Label)
            }
        }
    }
    abstract class ConstantType<I : Instruction, C>(
        private val valueCodec: Codec<C>,
        private val valueGetter: (I) -> C,
        private val constructor: (C) -> I,
    ) : InstructionType<I> {
        override val codec: Codec<I> = RecordCodecBuilder.create { instance ->
            instance.group(
                valueCodec.fieldOf("value").forGetter(valueGetter),
            ).apply(instance, constructor)
        }
    }
    // 0 -> 1
    data class ConstantString(val value: String) : Instruction {
        override val type = Type
        object Type : ConstantType<ConstantString, String>(Codec.STRING, ConstantString::value, ::ConstantString)
    }
    // 0 -> 1
    data class ConstantInt(val value: Int) : Instruction {
        override val type = Type
        object Type : ConstantType<ConstantInt, Int>(Codec.INT, ConstantInt::value, ::ConstantInt)
    }
    // 0 -> 1
    data class ConstantDouble(val value: Double) : Instruction {
        override val type = Type
        object Type : ConstantType<ConstantDouble, Double>(Codec.DOUBLE, ConstantDouble::value, ::ConstantDouble)
    }
    // 0 -> 1
    data class ConstantBool(val value: Boolean) : Instruction {
        override val type = Type
        object Type : ConstantType<ConstantBool, Boolean>(Codec.BOOL, ConstantBool::value, ::ConstantBool)
    }
    // 0 -> 1
    data class Arg(val name: String) : Instruction {
        override val type = Type
        object Type : InstructionType<Arg> {
            override val codec: Codec<Arg> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("name").forGetter(Arg::name),
                ).apply(instance, ::Arg)
            }
        }
    }
}
