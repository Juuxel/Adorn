package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.util.Logging;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public record GameRuleLootCondition(GameRule<?> gameRule) implements LootItemCondition {
    public static final MapCodec<GameRuleLootCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        BuiltInRegistries.GAME_RULE.byNameCodec()
            .fieldOf("game_rule")
            .forGetter(GameRuleLootCondition::gameRule)
    ).apply(builder, GameRuleLootCondition::new));
    private static final Logger LOGGER = Logging.logger();

    @Override
    public boolean test(LootContext lootContext) {
        var rule = lootContext.getLevel().getGameRules().get(gameRule);

        if (rule instanceof Boolean b) {
            return b;
        } else {
            LOGGER.error("Game rule {} ({}) is not a boolean", rule, gameRule);
        }

        return false;
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }

    public static Builder builder(GameRule<?> gameRule) {
        return new Builder(gameRule);
    }

    public static Builder builder(Registered<? extends GameRule<?>> gameRule) {
        return builder(gameRule.get());
    }

    public static final class Builder implements LootItemCondition.Builder {
        private final GameRule<?> gameRule;

        private Builder(GameRule<?> gameRule) {
            this.gameRule = gameRule;
        }

        @Override
        public LootItemCondition build() {
            return new GameRuleLootCondition(gameRule);
        }
    }
}
