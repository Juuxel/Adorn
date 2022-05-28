package juuxel.adorn.commonmixin;

import juuxel.adorn.block.PicketFenceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Block.class)
abstract class BlockMixin {
    @Inject(method = "sideCoversSmallSquare", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void adorn_onSideCoversSmallSquare(WorldView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> info, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof PicketFenceBlock && !((PicketFenceBlock) block).sideCoversSmallSquare(state)) {
            info.setReturnValue(false);
        }
    }
}
