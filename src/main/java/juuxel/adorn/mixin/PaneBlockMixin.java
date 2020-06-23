package juuxel.adorn.mixin;

import juuxel.adorn.block.ChainLinkFenceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PaneBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaneBlock.class)
abstract class PaneBlockMixin {
    // Mojang, why is this final?
    @Inject(method = "connectsTo", at = @At("RETURN"), cancellable = true)
    private void onConnectsTo(BlockState state, boolean bl, CallbackInfoReturnable<Boolean> info) {
        //noinspection ConstantConditions
        if ((Object) this instanceof ChainLinkFenceBlock && !info.getReturnValueZ()) {
            info.setReturnValue(state.getBlock() instanceof FenceGateBlock);
        }
    }
}
