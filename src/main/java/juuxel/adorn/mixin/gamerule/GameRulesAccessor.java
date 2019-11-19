package juuxel.adorn.mixin.gamerule;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    @Accessor("RULES")
    static Map<GameRules.RuleKey<?>, GameRules.RuleType<?>> getRules() {
        throw new AssertionError("@Accessor dummy body called");
    }

    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker
    static <T extends GameRules.Rule<T>> GameRules.RuleKey<T> callRegister(String name, GameRules.RuleType<T> type) {
        throw new AssertionError("@Invoker dummy body called");
    }
}
