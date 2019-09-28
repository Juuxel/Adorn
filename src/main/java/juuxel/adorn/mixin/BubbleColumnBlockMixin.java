package juuxel.adorn.mixin;

import juuxel.adorn.block.AdornBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ViewableWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BubbleColumnBlock.class)
public class BubbleColumnBlockMixin {
    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCanPlaceAt(BlockState state, ViewableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> info, Block downBlock) {
        if (!info.getReturnValueZ() && downBlock == AdornBlocks.PRISMARINE_CHIMNEY) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "calculateDrag", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void onCalculateDrag(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> info, BlockState state, Block block) {
        if (info.getReturnValueZ() && block == AdornBlocks.PRISMARINE_CHIMNEY) {
            info.setReturnValue(false);
        }
    }
}
