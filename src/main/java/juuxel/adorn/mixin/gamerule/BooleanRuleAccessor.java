package juuxel.adorn.mixin.gamerule;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.BooleanRule.class)
public interface BooleanRuleAccessor {
    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker
    static GameRules.RuleType<GameRules.BooleanRule> callOf(boolean value) {
        throw new AssertionError("@Invoker dummy body called");
    }

    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker
    static GameRules.RuleType<GameRules.BooleanRule> callOf(boolean value, BiConsumer<MinecraftServer, GameRules.BooleanRule> notifier) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
