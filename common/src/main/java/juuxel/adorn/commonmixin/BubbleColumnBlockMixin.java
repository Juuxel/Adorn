package juuxel.adorn.commonmixin;

import juuxel.adorn.block.PrismarineChimneyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleColumnBlock.class)
abstract class BubbleColumnBlockMixin {
    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void onCanPlaceAt(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockState stateBelow = world.getBlockState(pos.below());
        if (!info.getReturnValueZ() && stateBelow.getBlock() instanceof PrismarineChimneyBlock.WithColumn) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "getColumnState", at = @At("RETURN"), cancellable = true)
    private static void onGetBubbleState(Block bubbleColumn, BlockState belowState, BlockState occupyState, CallbackInfoReturnable<BlockState> info) {
        if (belowState.getBlock() instanceof PrismarineChimneyBlock.WithColumn chimney) {
            info.setReturnValue(Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(BubbleColumnBlock.DRAG_DOWN, chimney.getDrag()));
        }
    }
}
