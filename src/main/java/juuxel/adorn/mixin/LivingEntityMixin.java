package juuxel.adorn.mixin;

import juuxel.adorn.block.SeatBlock;
import juuxel.adorn.block.SofaBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow public abstract Optional<BlockPos> getSleepingPosition();

    @Inject(method = "isSleepingInBed", at = @At("HEAD"), cancellable = true)
    private void onIsSleepingInBed(CallbackInfoReturnable<Boolean> info) {
        if (getSleepingPosition().map(pos -> world.getBlockState(pos).getBlock() instanceof SofaBlock).orElse(false)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "method_18404", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onWakeUp(BlockPos pos, CallbackInfo info, BlockState state) {
        if (state.getBlock() instanceof SofaBlock) {
            world.setBlockState(pos, state.with(SeatBlock.OCCUPIED, false));
            BlockPos neighborPos = pos.offset(SofaBlock.Companion.getSleepingDirection(world, pos, true));
            world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(SeatBlock.OCCUPIED, false));
            Vec3d wakeUpPos = BedBlock.findWakeUpPosition(getType(), world, pos, 0).orElseGet(() -> {
                BlockPos upPos = pos.up();
                return new Vec3d((double)upPos.getX() + 0.5D, (double)upPos.getY() + 0.1D, (double)upPos.getZ() + 0.5D);
            });
            setPosition(wakeUpPos.x, wakeUpPos.y, wakeUpPos.z);
        }
    }
}
