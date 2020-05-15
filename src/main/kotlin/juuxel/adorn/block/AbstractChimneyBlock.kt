package juuxel.adorn.block

import juuxel.adorn.lib.AdornTags
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

abstract class AbstractChimneyBlock(settings: Settings) : Block(settings), Waterloggable {
    init {
        defaultState = defaultState
            .with(CONNECTED, false)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(CONNECTED, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.run {
            with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        }?.updateConnections(context.world.getBlockState(context.blockPos.up()))

    private fun BlockState.updateConnections(neighborState: BlockState): BlockState = run {
        with(CONNECTED, neighborState.isIn(AdornTags.CHIMNEYS.block))
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, side: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState = if (side == Direction.UP) state.updateConnections(neighborState) else state

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        if (state[CONNECTED]) MIDDLE_SHAPE else TOP_SHAPE

    companion object {
        val CONNECTED = BooleanProperty.of("connected")
        val WATERLOGGED = Properties.WATERLOGGED
        private val TOP_SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
        private val MIDDLE_SHAPE = createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0)
    }
}
