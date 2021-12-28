package juuxel.adorn.criterion

import com.google.gson.JsonObject
import juuxel.adorn.AdornCommon
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.item.ItemStack
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class BoughtFromTradingStationCriterion : AbstractCriterion<BoughtFromTradingStationCriterion.Conditions>() {
    override fun getId(): Identifier = ID

    override fun conditionsFromJson(
        json: JsonObject,
        playerPredicate: EntityPredicate.Extended,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions = Conditions(playerPredicate, ItemPredicate.fromJson(json["item"]))

    fun trigger(player: ServerPlayerEntity, soldItem: ItemStack) {
        trigger(player) { it.matches(soldItem) }
    }

    companion object {
        private val ID = AdornCommon.id("bought_from_trading_station")
    }

    class Conditions(playerPredicate: EntityPredicate.Extended, private val soldItem: ItemPredicate) :
        AbstractCriterionConditions(ID, playerPredicate) {
        fun matches(stack: ItemStack): Boolean =
            soldItem.test(stack)

        override fun toJson(predicateSerializer: AdvancementEntityPredicateSerializer): JsonObject {
            val json = super.toJson(predicateSerializer)
            json.add("sold_item", soldItem.toJson())
            return json
        }
    }
}
