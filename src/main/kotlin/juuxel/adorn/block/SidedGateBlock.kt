package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.minecraft.block.*
import net.minecraft.block.enums.DoorHinge
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties

class SidedGateBlock(
    override val name: String,
    override val itemSettings: Item.Settings?,
    settings: Settings
) : FenceGateBlock(settings), Waterloggable, PolyesterBlock {
    init {
        defaultState = defaultState
            .with(HINGE, DoorHinge.LEFT)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(HINGE, WATERLOGGED)
    }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getPlacementState(context: ItemPlacementContext) = super.getPlacementState(context)?.let { state ->
        // TODO: Hinge (see DoorBlock.getHinge)
        state
    }

    companion object {
        val HINGE = Properties.DOOR_HINGE
        val WATERLOGGED = Properties.WATERLOGGED
    }
}
