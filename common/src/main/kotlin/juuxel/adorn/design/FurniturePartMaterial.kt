package juuxel.adorn.design

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSets
import net.minecraft.text.Text
import net.minecraft.util.Identifier

sealed interface FurniturePartMaterial {
    val id: Identifier
    val type: Type
    val displayName: Text get() = Text.translatable("furniture_material.adorn.${getTranslationKey(id)}")

    companion object {
        val CODEC: Codec<FurniturePartMaterial> = Type.CODEC.dispatch({ it.type }, { it.codec() })

        private fun getTranslationKey(id: Identifier): String {
            if (id.namespace == AdornCommon.NAMESPACE) return id.path
            return id.toShortTranslationKey()
        }
    }

    enum class Type(private val id: String, val codec: () -> Codec<out FurniturePartMaterial>) {
        BLOCK_VARIANT("block_variant", { OfVariant.CODEC }),
        FUNCTIONAL("functional", { Functional.CODEC });

        companion object {
            private val BY_ID = values().associateBy { it.id }
            val CODEC: Codec<Type> = Codec.STRING.comapFlatMap(
                { id -> DataResult.success(BY_ID[id]) ?: DataResult.error("Unknown furniture part material type: $id") },
                { it.id }
            )
        }
    }

    data class OfVariant(val variant: BlockVariant) : FurniturePartMaterial {
        override val type = Type.BLOCK_VARIANT
        override val id get() = BlockVariantSets.getId(variant)

        companion object {
            val CODEC: Codec<OfVariant> = BlockVariant.CODEC.xmap(::OfVariant, OfVariant::variant)
        }
    }

    enum class Functional(override val id: Identifier) : FurniturePartMaterial {
        SEAT("seat");

        override val type = Type.FUNCTIONAL

        constructor(id: String) : this(AdornCommon.id(id))

        companion object {
            private val BY_ID = values().associateBy { it.id }
            val CODEC: Codec<Functional> = Identifier.CODEC.comapFlatMap(
                { id -> DataResult.success(BY_ID[id]) ?: DataResult.error("Unknown furniture part material type: $id") },
                { it.id }
            )
        }
    }
}
