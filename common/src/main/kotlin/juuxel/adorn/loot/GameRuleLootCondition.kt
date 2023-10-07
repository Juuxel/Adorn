package juuxel.adorn.loot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.logger
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
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
        val CODEC: Codec<GameRuleLootCondition> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.STRING.fieldOf("game_rule").forGetter { it.gameRule }
            ).apply(builder, ::GameRuleLootCondition)
        }
        private val LOGGER = logger()
    }
}
