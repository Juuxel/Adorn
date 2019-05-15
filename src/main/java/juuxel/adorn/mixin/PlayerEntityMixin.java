package juuxel.adorn.mixin;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;wakeUp(ZZZ)V", ordinal = 0))
    private void redirectWakeUp(PlayerEntity player, boolean sleepTimeSomething, boolean updatePlayersSleeping, boolean updateSpawn) {
        Block sleepingBlock = player.getSleepingPosition()
                .map(pos -> world.getBlockState(pos).getBlock())
                .orElse(Blocks.AIR);
        if (sleepingBlock instanceof BedBlock) {
            player.wakeUp(sleepTimeSomething, updatePlayersSleeping, updateSpawn);
        }

        // Allow sleeping on sofas at daytime
    }

    @Inject(method = "isSleepingLongEnough", at = @At("RETURN"), cancellable = true)
    private void onIsSleepingLongEnough(CallbackInfoReturnable<Boolean> info) {
        // Allow sleeping on sofas at daytime & prevent skipping the night on sofas
        if (info.getReturnValue() && getSleepingPosition().map(pos -> world.getBlockState(pos).getBlock() instanceof SofaBlock).orElse(false)) {
            info.setReturnValue(false);
        }
    }
}
