package juuxel.adorn.commonmixin;

import juuxel.adorn.block.AdornBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.FallLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallLocation.class)
abstract class FallLocationMixin {
    @Inject(method = "blockToFallLocation", at = @At("HEAD"), cancellable = true)
    private static void allowStoneLadders(BlockState state, CallbackInfoReturnable<FallLocation> info) {
        if (state.is(AdornBlocks.STONE_LADDER.get())) {
            info.setReturnValue(FallLocation.LADDER);
        }
    }
}
