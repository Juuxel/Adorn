package juuxel.adorn.commonmixin;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    private int sleepTimer;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZ)V", ordinal = 0))
    private void redirectWakeUp(PlayerEntity player, boolean sleepTimeSomething, boolean updatePlayersSleeping) {
        Block sleepingBlock = player.getSleepingPosition()
                .map(pos -> world.getBlockState(pos).getBlock())
                .orElse(Blocks.AIR);
        if (!(sleepingBlock instanceof SofaBlock)) {
            player.wakeUp(sleepTimeSomething, updatePlayersSleeping);
        } else if (world.getGameRules().getBoolean(AdornGameRules.SKIP_NIGHT_ON_SOFAS)) {
            // Decrease the timer during daylight. If the player *has* slept before (= at night), they will wake up.
            if (--sleepTimer > 0) {
                player.wakeUp(sleepTimeSomething, updatePlayersSleeping);
            }
        }
    }

    @Inject(method = "isSleepingLongEnough", at = @At("RETURN"), cancellable = true)
    private void onIsSleepingLongEnough(CallbackInfoReturnable<Boolean> info) {
        // Allow sleeping on sofas at daytime and (depending on config)
        // prevent skipping the night on sofas
        boolean skipNight = world.getGameRules().getBoolean(AdornGameRules.SKIP_NIGHT_ON_SOFAS);
        if (info.getReturnValueZ() && (!skipNight || world.isDay()) &&
                getSleepingPosition().map(pos -> world.getBlockState(pos).getBlock() instanceof SofaBlock).orElse(false)) {
            info.setReturnValue(false);
        }
    }
}
