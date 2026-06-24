package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.util.Logging;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registries;
import net.minecraft.world.rule.GameRule;
import org.slf4j.Logger;

public record GameRuleLootCondition(GameRule<?> gameRule) implements LootCondition {
    public static final MapCodec<GameRuleLootCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Registries.GAME_RULE.getCodec()
            .fieldOf("game_rule")
            .forGetter(GameRuleLootCondition::gameRule)
    ).apply(builder, GameRuleLootCondition::new));
    private static final Logger LOGGER = Logging.logger();

    @Override
    public boolean test(LootContext lootContext) {
        var rule = lootContext.getWorld().getGameRules().getValue(gameRule);

        if (rule instanceof Boolean b) {
            return b;
        } else {
            LOGGER.error("Game rule {} ({}) is not a boolean", rule, gameRule);
        }

        return false;
    }

    @Override
    public LootConditionType getType() {
        return AdornLootConditionTypes.GAME_RULE.get();
    }

    public static Builder builder(GameRule<?> gameRule) {
        return new Builder(gameRule);
    }

    public static Builder builder(Registered<? extends GameRule<?>> gameRule) {
        return builder(gameRule.get());
    }

    public static final class Builder implements LootCondition.Builder {
        private final GameRule<?> gameRule;

        private Builder(GameRule<?> gameRule) {
            this.gameRule = gameRule;
        }

        @Override
        public LootCondition build() {
            return new GameRuleLootCondition(gameRule);
        }
    }
}
