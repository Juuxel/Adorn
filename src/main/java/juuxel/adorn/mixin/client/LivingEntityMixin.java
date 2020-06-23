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
    public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "getSleepingDirection", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onGetSleepingDirection(CallbackInfoReturnable<Direction> info, BlockPos pos) {
        if (info.getReturnValue() == null && pos != null) {
            try {
                info.setReturnValue(SofaBlock.Companion.getSleepingDirection(world, pos, true).getOpposite());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
