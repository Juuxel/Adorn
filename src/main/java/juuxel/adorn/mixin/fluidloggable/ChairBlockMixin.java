package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.ChairBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.towelette.api.FluidProperty;

@Mixin(ChairBlock.class)
public class ChairBlockMixin {
    @Inject(method = "updateFluidPropertyOnPlaced", at = @At("RETURN"), cancellable = true, remap = false)
    private void onUpdateFluidPropertyOnPlaced(BlockState state, FluidState fluidState, CallbackInfoReturnable<BlockState> info) {
        info.setReturnValue(info.getReturnValue().with(FluidProperty.FLUID, FluidProperty.FLUID.of(fluidState)));
    }
}
