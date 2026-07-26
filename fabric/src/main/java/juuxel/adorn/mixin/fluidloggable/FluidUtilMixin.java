package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.FluidUtil;
// import net.minecraft.world.level.block.state.BlockState;
// import net.minecraft.world.level.material.FluidState;
// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// import virtuoel.towelette.api.FluidProperties;
// import virtuoel.towelette.api.Fluidloggable;

@Mixin(FluidUtil.class)
abstract class FluidUtilMixin {
    // @Inject(method = "updateFluidFromState", at = @At("RETURN"), cancellable = true, remap = false)
    // private static void onUpdateFluidFromState(BlockState state, FluidState fluidState, CallbackInfoReturnable<BlockState> info) {
    //     if (state.getBlock() instanceof Fluidloggable) {
    //         BlockState blockState = info.getReturnValue().setValue(FluidProperties.FLUID, BuiltInRegistries.FLUID.getKey(fluidState.getType()));
    //
    //         if (blockState.hasProperty(FluidProperties.FALLING)) {
    //             blockState = blockState.setValue(FluidProperties.FALLING, fluidState.getValues().containsKey(BlockStateProperties.FALLING) && fluidState.getValue(BlockStateProperties.FALLING));
    //         }
    //
    //         if (blockState.hasProperty(FluidProperties.LEVEL_1_8)) {
    //             blockState = blockState.setValue(FluidProperties.LEVEL_1_8, fluidState.getValues().containsKey(BlockStateProperties.LEVEL_FLOWING) ? fluidState.getValue(BlockStateProperties.LEVEL_FLOWING) : 8);
    //         }
    //
    //         info.setReturnValue(blockState);
    //     }
    // }
}
