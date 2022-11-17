package juuxel.adorn.fluid

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Decoder
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import net.minecraft.fluid.Fluid
import net.minecraft.network.PacketByteBuf
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.registry.Registry

/**
 * A "key" that identifies a fluid or a group of fluids.
 *
 * Could be a single fluid, a tag or a list of the former.
 *
 * ## JSON format
 *
 * A fluid key is one of:
 *
 * - a string; if prefixed with `#`, a tag, otherwise a fluid ID
 * - an array of strings as described above
 *
 * Examples: `"minecraft:water"`, `"#c:milk"`, `["minecraft:water", "minecraft:lava"]`
 */
sealed class FluidKey {
    /**
     * Returns the set of all fluids matching this key.
     */
    abstract fun getFluids(): Set<Fluid>

    /**
     * Tests whether the [fluid] matches this key.
     */
    abstract fun matches(fluid: Fluid): Boolean

    /**
     * Writes this key to a packet buffer.
     * @see load
     */
    fun write(buf: PacketByteBuf) {
        val fluids = getFluids()
        buf.writeVarInt(fluids.size)

        for (fluid in fluids) {
            buf.writeVarInt(Registry.FLUID.getRawId(fluid))
        }
    }

    companion object {
        private val SIMPLE_ENCODER = object : Encoder<Simple> {
            override fun <T> encode(input: Simple, ops: DynamicOps<T>, prefix: T): DataResult<T> =
                ops.mergeToPrimitive(prefix, ops.createString(input.id))
        }

        private val SIMPLE_DECODER = object : Decoder<Simple> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Simple, T>> =
                ops.getStringValue(input)
                    .map {
                        if (it.startsWith('#')) {
                            OfTag(TagKey.of(Registry.FLUID_KEY, Identifier(it.substring(1))))
                        } else {
                            OfFluid(Registry.FLUID[Identifier(it)])
                        }
                    }
                    .map { Pair.of(it, ops.empty()) }
        }

        private val SIMPLE_CODEC: Codec<Simple> = Codec.of(SIMPLE_ENCODER, SIMPLE_DECODER, "SimpleFluidKey")

        val CODEC: Codec<FluidKey> = Codecs.xor(
            SIMPLE_CODEC,
            SIMPLE_CODEC.listOf().xmap({ OfArray(it) }, { it.children })
        ).xmap(
            { it.map({ x -> x }, { x -> x }) },
            {
                when (it) {
                    is Simple -> Either.left(it)
                    is OfArray -> Either.right(it)
                }
            }
        )

        /**
         * Reads a key from a packet buffer.
         * @see write
         */
        fun load(buf: PacketByteBuf): FluidKey {
            val size = buf.readVarInt()
            fun readFluid(): Fluid = Registry.FLUID[buf.readVarInt()]

            return if (size == 1) {
                OfFluid(readFluid())
            } else {
                OfArray(
                    (1..size).map {
                        OfFluid(readFluid())
                    }
                )
            }
        }
    }

    private sealed class Simple : FluidKey() {
        abstract val id: String
    }

    private data class OfFluid(val fluid: Fluid) : Simple() {
        override val id: String by lazy { Registry.FLUID.getId(fluid).toString() }
        override fun getFluids() = setOf(fluid)

        override fun matches(fluid: Fluid): Boolean =
            fluid === this.fluid
    }

    private data class OfTag(val tag: TagKey<Fluid>) : Simple() {
        override val id = "#${tag.id}"

        override fun getFluids(): Set<Fluid> =
            Registry.FLUID.getOrCreateEntryList(tag).mapTo(HashSet()) { it.value() }

        override fun matches(fluid: Fluid): Boolean =
            fluid.isIn(tag)
    }

    private data class OfArray(val children: List<Simple>) : FluidKey() {
        override fun getFluids(): Set<Fluid> =
            children.flatMapTo(HashSet()) { it.getFluids() }

        override fun matches(fluid: Fluid): Boolean =
            children.any { it.matches(fluid) }
    }
}
