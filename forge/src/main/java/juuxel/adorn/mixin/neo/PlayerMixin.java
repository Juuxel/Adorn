package juuxel.adorn.mixin.neo;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {
    private PlayerMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = "isSleepingLongEnough", at = @At("RETURN"), cancellable = true)
    private void onCanResetTimeBySleeping(CallbackInfoReturnable<Boolean> info) {
        // Allow sleeping on sofas at daytime and (depending on config)
        // prevent skipping the night on sofas
        if (!(level() instanceof ServerLevel world)) return;
        boolean skipNight = world.getGameRules().get(AdornGameRules.SKIP_NIGHT_ON_SOFAS.get());
        if (info.getReturnValueZ() && (!skipNight || world.isBrightOutside()) &&
            getSleepingPos().map(pos -> world.getBlockState(pos).getBlock() instanceof SofaBlock).orElse(false)) {
            info.setReturnValue(false);
        }
    }
}
