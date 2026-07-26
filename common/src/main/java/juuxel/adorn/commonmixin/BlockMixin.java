package juuxel.adorn.commonmixin;

import com.llamalad7.mixinextras.sugar.Local;
import juuxel.adorn.block.PicketFenceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
abstract class BlockMixin {
    @Inject(method = "canSupportCenter", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/LevelReader;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), cancellable = true)
    private static void adorn_onSideCoversSmallSquare(LevelReader world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> info, @Local BlockState state) {
        Block block = state.getBlock();
        if (block instanceof PicketFenceBlock picketFence && !picketFence.sideCoversSmallSquare(state)) {
            info.setReturnValue(false);
        }
    }
}
