package juuxel.adorn.mixin.client;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getSleepingDirection", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onGetSleepingDirection(CallbackInfoReturnable<Direction> info, BlockPos pos) {
        if (info.getReturnValue() == null && pos != null) {
            Direction direction = SofaBlock.getSleepingDirection(world, pos, true);

            if (direction != null) {
                info.setReturnValue(direction.getOpposite());
            }
        }
    }
}
