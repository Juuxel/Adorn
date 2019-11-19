package juuxel.adorn.mixin.gamerule;

import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRules.class)
public class GameRulesMixin {
    static {
        AdornGameRules.init();
    }
}
