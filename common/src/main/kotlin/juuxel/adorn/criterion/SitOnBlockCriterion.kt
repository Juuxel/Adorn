package juuxel.adorn.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.predicate.BlockPredicate
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.dynamic.Codecs
import net.minecraft.util.math.BlockPos
import java.util.Optional

class SitOnBlockCriterion : AbstractCriterion<SitOnBlockCriterion.Conditions>() {
    override fun getConditionsCodec(): Codec<Conditions> = Conditions.CODEC

    fun trigger(player: ServerPlayerEntity, pos: BlockPos) {
        trigger(player) { it.matches(player, pos) }
    }

    data class Conditions(val playerPredicate: Optional<LootContextPredicate>, val block: BlockPredicate) : AbstractCriterion.Conditions {
        override fun player(): Optional<LootContextPredicate> = playerPredicate

        fun matches(player: ServerPlayerEntity, pos: BlockPos): Boolean =
            block.test(player.serverWorld, pos)

        companion object {
            val CODEC: Codec<Conditions> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                        .forGetter(Conditions::playerPredicate),
                    BlockPredicate.CODEC.fieldOf("block").forGetter(Conditions::block)
                ).apply(instance, ::Conditions)
            }
        }
    }
}
