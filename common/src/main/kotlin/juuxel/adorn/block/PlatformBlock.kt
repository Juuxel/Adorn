@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class PlatformBlock(variant: BlockVariant) : Block(variant.createSettings()), BlockWithDescription, Waterloggable {
    override val descriptionKey = "block.adorn.platform.description"

    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        defaultState.with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: ShapeContext?): VoxelShape =
        COMBINED_SHAPE

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState, world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    companion object {
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        private val PLATFORM_SHAPE = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        private val COMBINED_SHAPE = VoxelShapes.union(PostBlock.Y_SHAPE, PLATFORM_SHAPE)
    }
}
