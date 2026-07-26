package juuxel.adorn.lib;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import juuxel.adorn.config.Config;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

import java.util.function.Predicate;

public final class AdornGameRules {
    public static final Registrar<GameRule<?>> GAME_RULES = RegistrarFactory.get().create(Registries.GAME_RULE);
    public static final Registered<GameRule<Boolean>> SKIP_NIGHT_ON_SOFAS =
        registerBoolean("skip_night_on_sofas", GameRuleCategory.PLAYER, defaults -> defaults.skipNightOnSofas);
    public static final Registered<GameRule<Boolean>> INFINITE_KITCHEN_SINKS =
        registerBoolean("infinite_kitchen_sinks", GameRuleCategory.MISC, defaults -> defaults.infiniteKitchenSinks);
    public static final Registered<GameRule<Boolean>> DROP_LOCKED_TRADING_STATIONS =
        registerBoolean("drop_locked_trading_stations", GameRuleCategory.DROPS, defaults -> defaults.dropLockedTradingStations);

    public static void init() {
    }

    private static Registered<GameRule<Boolean>> registerBoolean(String name, GameRuleCategory category, Predicate<Config.GameRuleDefaults> defaultGetter) {
        return GAME_RULES.register(name, () -> createBooleanRule(category, defaultGetter));
    }

    private static GameRule<Boolean> createBooleanRule(GameRuleCategory category, Predicate<Config.GameRuleDefaults> defaultGetter) {
        boolean defaultValue = defaultGetter.test(ConfigManager.config().gameRuleDefaults);
        return new GameRule<>(
            category,
            GameRuleType.BOOL,
            BoolArgumentType.bool(),
            GameRuleTypeVisitor::visitBoolean,
            Codec.BOOL,
            (value) -> value ? Command.SINGLE_SUCCESS : 0,
            defaultValue,
            FeatureFlagSet.of()
        );
    }
}
