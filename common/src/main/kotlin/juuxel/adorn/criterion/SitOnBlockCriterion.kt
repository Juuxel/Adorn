package juuxel.adorn.criterion

import com.google.gson.JsonObject
import juuxel.adorn.AdornCommon
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.BlockPredicate
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

class SitOnBlockCriterion : AbstractCriterion<SitOnBlockCriterion.Conditions>() {
    override fun getId(): Identifier = ID

    override fun conditionsFromJson(
        json: JsonObject,
        playerPredicate: EntityPredicate.Extended,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions = Conditions(playerPredicate, BlockPredicate.fromJson(json["block"]))

    fun trigger(player: ServerPlayerEntity, pos: BlockPos) {
        trigger(player) { it.matches(player, pos) }
    }

    companion object {
        private val ID = AdornCommon.id("sit_on_block")
    }

    class Conditions(playerPredicate: EntityPredicate.Extended, private val block: BlockPredicate) :
        AbstractCriterionConditions(ID, playerPredicate) {
        fun matches(player: ServerPlayerEntity, pos: BlockPos): Boolean =
            block.test(player.getWorld(), pos)

        override fun toJson(predicateSerializer: AdvancementEntityPredicateSerializer): JsonObject {
            val json = super.toJson(predicateSerializer)
            json.add("block", block.toJson())
            return json
        }
    }
}
