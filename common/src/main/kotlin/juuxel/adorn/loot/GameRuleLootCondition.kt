package juuxel.adorn.loot

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import juuxel.adorn.util.logger
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.util.JsonHelper
import net.minecraft.util.JsonSerializer
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.BooleanRule

class GameRuleLootCondition(private val gameRule: String) : LootCondition {
    private val key: GameRules.Key<*> = GameRules.Key(gameRule, GameRules.Category.MISC)

    override fun test(context: LootContext): Boolean =
        when (val rule = context.world.gameRules.get(key)) {
            is BooleanRule -> rule.get()

            null -> {
                LOGGER.error("Unknown game rule {} in loot condition", gameRule)
                false
            }

            else -> {
                LOGGER.error("Game rule {} ({}) is not a boolean", rule, gameRule)
                false
            }
        }

    override fun getType(): LootConditionType =
        AdornLootConditionTypes.GAME_RULE

    companion object {
        private val LOGGER = logger()
    }

    object Serializer : JsonSerializer<GameRuleLootCondition> {
        override fun toJson(json: JsonObject, condition: GameRuleLootCondition, context: JsonSerializationContext) {
            json.addProperty("game_rule", condition.gameRule)
        }

        override fun fromJson(json: JsonObject, context: JsonDeserializationContext): GameRuleLootCondition =
            GameRuleLootCondition(JsonHelper.getString(json, "game_rule"))
    }
}
