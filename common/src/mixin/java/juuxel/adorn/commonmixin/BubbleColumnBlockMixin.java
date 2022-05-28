package juuxel.adorn.commonmixin;

import juuxel.adorn.block.PrismarineChimneyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleColumnBlock.class)
abstract class BubbleColumnBlockMixin {
    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
    private void onCanPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockState stateBelow = world.getBlockState(pos.down());
        if (!info.getReturnValueZ() && stateBelow.getBlock() instanceof PrismarineChimneyBlock.WithColumn) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "getBubbleState", at = @At("RETURN"), cancellable = true)
    private static void onGetBubbleState(BlockState state, CallbackInfoReturnable<BlockState> info) {
        Block block = state.getBlock();
        if (block instanceof PrismarineChimneyBlock.WithColumn chimney) {
            info.setReturnValue(Blocks.BUBBLE_COLUMN.getDefaultState().with(BubbleColumnBlock.DRAG, chimney.getDrag()));
        }
    }
}
