package juuxel.adorn.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class FluidUtil {
    /**
     * For using a mixin (juuxel.adorn.mixin.fluidloggable.FluidUtilMixin) to set the fluid property
     * for Towelette support.
     */
    public static BlockState updateFluidFromState(BlockState state, FluidState fluidState) {
        return state.setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }
}
