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
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class PostBlock(variant: BlockVariant) : Block(variant.createSettings()), BlockWithDescription, Waterloggable {
    override val descriptionKey = "block.adorn.post.description"

    init {
        defaultState = defaultState.with(AXIS, Direction.Axis.Y).with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(AXIS, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        defaultState.with(AXIS, context.side.axis).with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        when (state[AXIS]) {
            Direction.Axis.X -> X_SHAPE
            Direction.Axis.Y -> Y_SHAPE
            Direction.Axis.Z -> Z_SHAPE
            else -> throw IllegalStateException("Axis of $state is null??")
        }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        if (rotation == BlockRotation.COUNTERCLOCKWISE_90 || rotation == BlockRotation.CLOCKWISE_90) {
            return when (state[AXIS]) {
                Direction.Axis.X -> state.with(AXIS, Direction.Axis.Z)
                Direction.Axis.Z -> state.with(AXIS, Direction.Axis.X)
                else -> state
            }
        }

        return state
    }

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState, world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    companion object {
        val AXIS: EnumProperty<Direction.Axis> = Properties.AXIS
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        val X_SHAPE: VoxelShape = createCuboidShape(0.0, 6.0, 6.0, 16.0, 10.0, 10.0)
        val Y_SHAPE: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
        val Z_SHAPE: VoxelShape = createCuboidShape(6.0, 6.0, 0.0, 10.0, 10.0, 16.0)
    }
}
