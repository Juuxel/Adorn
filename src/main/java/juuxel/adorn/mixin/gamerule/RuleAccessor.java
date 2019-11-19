package juuxel.adorn.mixin.gamerule;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.Rule.class)
public interface RuleAccessor {
    @Invoker
    void callSetFromString(String value);

    @Invoker
    String callValueToString();
}
