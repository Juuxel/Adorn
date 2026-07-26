package juuxel.adorn.commonmixin;

import juuxel.adorn.block.PicketFenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
abstract class FenceGateBlockMixin {
    @Inject(method = "isWall", at = @At("HEAD"), cancellable = true)
    private void onIsWall(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof PicketFenceBlock) {
            info.setReturnValue(true);
        }
    }
}
