package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.FluidUtil;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import virtuoel.towelette.api.FluidProperties;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(FluidUtil.class)
abstract class FluidUtilMixin {
    @Inject(method = "updateFluidFromState", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onUpdateFluidFromState(BlockState state, FluidState fluidState, CallbackInfoReturnable<BlockState> info) {
        if (state.getBlock() instanceof Fluidloggable) {
            BlockState blockState = info.getReturnValue().with(FluidProperties.FLUID, Registries.FLUID.getId(fluidState.getFluid()));

            if (blockState.contains(FluidProperties.FALLING)) {
                blockState = blockState.with(FluidProperties.FALLING, fluidState.getEntries().containsKey(Properties.FALLING) && fluidState.get(Properties.FALLING));
            }

            if (blockState.contains(FluidProperties.LEVEL_1_8)) {
                blockState = blockState.with(FluidProperties.LEVEL_1_8, fluidState.getEntries().containsKey(Properties.LEVEL_1_8) ? fluidState.get(Properties.LEVEL_1_8) : 8);
            }

            info.setReturnValue(blockState);
        }
    }
}
