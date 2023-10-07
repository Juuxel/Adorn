package juuxel.adorn.criterion

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.mojang.serialization.JsonOps
import juuxel.adorn.util.logger
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.BlockPredicate
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import java.util.Optional

class SitOnBlockCriterion : AbstractCriterion<SitOnBlockCriterion.Conditions>() {
    override fun conditionsFromJson(
        json: JsonObject,
        playerPredicate: Optional<LootContextPredicate>,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): Conditions = Conditions(playerPredicate, Util.getResult(BlockPredicate.CODEC.parse(JsonOps.INSTANCE, json["block"]), ::JsonParseException))

    fun trigger(player: ServerPlayerEntity, pos: BlockPos) {
        trigger(player) { it.matches(player, pos) }
    }

    companion object {
        private val LOGGER = logger()
    }

    class Conditions(playerPredicate: Optional<LootContextPredicate>, private val block: BlockPredicate) :
        AbstractCriterionConditions(playerPredicate) {
        fun matches(player: ServerPlayerEntity, pos: BlockPos): Boolean =
            block.test(player.serverWorld, pos)

        override fun toJson(): JsonObject {
            val json = super.toJson()
            json.add("block", BlockPredicate.CODEC.encodeStart(JsonOps.INSTANCE, block).getOrThrow(false) {
                LOGGER.error("Could not encode block predicate {}: {}", block, it)
            })
            return json
        }
    }
}
