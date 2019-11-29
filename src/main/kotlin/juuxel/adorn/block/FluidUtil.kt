package juuxel.adorn.block

import net.minecraft.block.BlockState
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.state.property.Properties

object FluidUtil {
    /**
     * For using a mixin ([juuxel.adorn.mixin.fluidloggable.FluidUtilMixin]) to set the fluid property
     * for Towelette support.
     */
    @JvmStatic
    fun updateFluidFromState(state: BlockState, fluidState: FluidState) =
        state.with(Properties.WATERLOGGED, fluidState.fluid == Fluids.WATER)
}
