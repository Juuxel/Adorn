package juuxel.adorn.criterion

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.item.ItemStack
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.server.network.ServerPlayerEntity
import java.util.Optional

class BoughtFromTradingStationCriterion : AbstractCriterion<BoughtFromTradingStationCriterion.Conditions>() {
    override fun conditionsFromJson(
        json: JsonObject,
        playerPredicate: Optional<LootContextPredicate>,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions = Conditions(playerPredicate, ItemPredicate.fromJson(json["item"]).orElseThrow {
        JsonParseException("Missing item in bought_from_trading_station criterion")
    })

    fun trigger(player: ServerPlayerEntity, soldItem: ItemStack) {
        trigger(player) { it.matches(soldItem) }
    }

    class Conditions(playerPredicate: Optional<LootContextPredicate>, private val soldItem: ItemPredicate) :
        AbstractCriterionConditions(playerPredicate) {
        fun matches(stack: ItemStack): Boolean =
            soldItem.test(stack)

        override fun toJson(): JsonObject {
            val json = super.toJson()
            json.add("sold_item", soldItem.toJson())
            return json
        }
    }
}
