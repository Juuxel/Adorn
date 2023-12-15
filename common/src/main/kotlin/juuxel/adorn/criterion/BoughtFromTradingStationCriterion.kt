package juuxel.adorn.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.item.ItemStack
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.dynamic.Codecs
import java.util.Optional

class BoughtFromTradingStationCriterion : AbstractCriterion<BoughtFromTradingStationCriterion.Conditions>() {
    override fun getConditionsCodec(): Codec<Conditions> = Conditions.CODEC

    fun trigger(player: ServerPlayerEntity, soldItem: ItemStack) {
        trigger(player) { it.matches(soldItem) }
    }

    data class Conditions(val playerPredicate: Optional<LootContextPredicate>, val soldItem: Optional<ItemPredicate>) : AbstractCriterion.Conditions {
        override fun player(): Optional<LootContextPredicate> = playerPredicate

        fun matches(stack: ItemStack): Boolean =
            soldItem.map { it.test(stack) }.orElse(true)

        companion object {
            val CODEC: Codec<Conditions> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                        .forGetter(Conditions::playerPredicate),
                    Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "item")
                        .forGetter(Conditions::soldItem)
                ).apply(instance, ::Conditions)
            }
        }
    }
}
