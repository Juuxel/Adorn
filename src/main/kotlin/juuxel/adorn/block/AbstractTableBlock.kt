@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

abstract class AbstractTableBlock(settings: Settings) : CarpetedBlock(settings), Waterloggable {
    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    protected abstract fun canConnectTo(state: BlockState, sideOfSelf: Direction): Boolean

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        updateConnections(
            super.getPlacementState(context)!!
                .with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER),
            context.world,
            context.blockPos
        )

    override fun getFluidState(state: BlockState) =
        if (state[Properties.WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ) = updateConnections(
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos),
        world,
        pos
    )

    private fun updateConnections(
        state: BlockState,
        world: WorldAccess,
        pos: BlockPos
    ) = state.with(NORTH, canConnectTo(world.getBlockState(pos.offset(Direction.NORTH)), Direction.NORTH))
        .with(EAST, canConnectTo(world.getBlockState(pos.offset(Direction.EAST)), Direction.EAST))
        .with(SOUTH, canConnectTo(world.getBlockState(pos.offset(Direction.SOUTH)), Direction.SOUTH))
        .with(WEST, canConnectTo(world.getBlockState(pos.offset(Direction.WEST)), Direction.WEST))

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        getShapeForKey(
            Bits.buildTableState(
                state[NORTH], state[EAST], state[SOUTH], state[WEST],
                isCarpetingEnabled() && state[CARPET].isPresent
            )
        )

    protected abstract fun getShapeForKey(key: Byte): VoxelShape

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    companion object {
        val NORTH = Properties.NORTH
        val EAST = Properties.EAST
        val SOUTH = Properties.SOUTH
        val WEST = Properties.WEST
        val CARPET = CarpetedBlock.CARPET
        val WATERLOGGED = Properties.WATERLOGGED
    }
}
