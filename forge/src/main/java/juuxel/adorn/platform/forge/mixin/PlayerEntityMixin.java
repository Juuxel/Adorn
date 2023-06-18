package juuxel.adorn.platform.forge.mixin;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "canResetTimeBySleeping", at = @At("RETURN"), cancellable = true)
    private void onCanResetTimeBySleeping(CallbackInfoReturnable<Boolean> info) {
        // Allow sleeping on sofas at daytime and (depending on config)
        // prevent skipping the night on sofas
        var world = getWorld();
        boolean skipNight = world.getGameRules().getBoolean(AdornGameRules.SKIP_NIGHT_ON_SOFAS);
        if (info.getReturnValueZ() && (!skipNight || world.isDay()) &&
            getSleepingPosition().map(pos -> world.getBlockState(pos).getBlock() instanceof SofaBlock).orElse(false)) {
            info.setReturnValue(false);
        }
    }
}
