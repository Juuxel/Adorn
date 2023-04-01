@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.util.buildShapeRotationsFromNorth
import juuxel.adorn.util.mergeIntoShapeMap
import juuxel.adorn.util.mergeShapeMaps
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class PicketFenceBlock(settings: Settings) : Block(settings), Waterloggable, BlockWithDescription {
    init {
        defaultState = defaultState
            .with(FACING, Direction.NORTH)
            .with(SHAPE, Shape.STRAIGHT)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(SHAPE, FACING, WATERLOGGED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)
            .with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER)
            .let { updateShape(ctx.world, ctx.blockPos, it) }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, facing: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (facing.axis == state[FACING].axis) {
            return updateShape(world, pos, state)
        }

        return state
    }

    private fun updateShape(world: WorldAccess, pos: BlockPos, state: BlockState): BlockState {
        val fenceFacing = state[FACING]
        for (side in arrayOf(fenceFacing.opposite, fenceFacing)) {
            val inner = side == fenceFacing
            val neighborState = world.getBlockState(pos.offset(side))
            val neighborBlock = neighborState.block
            val neighborFacing = if (neighborBlock is PicketFenceBlock) neighborState[FACING] else null

            var shape = when (neighborFacing) {
                fenceFacing.rotateYClockwise() -> {
                    if (inner) {
                        Shape.CLOCKWISE_INNER_CORNER
                    } else {
                        Shape.CLOCKWISE_CORNER
                    }
                }
                fenceFacing.rotateYCounterclockwise() -> {
                    if (inner) {
                        Shape.COUNTERCLOCKWISE_INNER_CORNER
                    } else {
                        Shape.COUNTERCLOCKWISE_CORNER
                    }
                }
                else -> Shape.STRAIGHT
            }

            // Prevent funny connections
            if (neighborBlock !is PicketFenceBlock || !neighborBlock.connectsTo(neighborState, side.opposite)) {
                shape = Shape.STRAIGHT
            }

            if (shape != Shape.STRAIGHT) {
                return state.with(SHAPE, shape)
            }
        }

        return state.with(SHAPE, Shape.STRAIGHT)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = when (state[SHAPE]!!) {
        Shape.STRAIGHT -> STRAIGHT_OUTLINE_SHAPES.getValue(state[FACING])
        Shape.CLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.getValue(state[FACING])
        Shape.COUNTERCLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.getValue(state[FACING].rotateYCounterclockwise())
        Shape.CLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.getValue(state[FACING].opposite)
        Shape.COUNTERCLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.getValue(state[FACING].rotateYClockwise())
    }

    override fun getCollisionShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        when (state[SHAPE]!!) {
            Shape.STRAIGHT -> STRAIGHT_COLLISION_SHAPES.getValue(state[FACING])
            Shape.CLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.getValue(state[FACING])
            Shape.COUNTERCLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.getValue(state[FACING].rotateYCounterclockwise())
            Shape.CLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.getValue(state[FACING].opposite)
            Shape.COUNTERCLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.getValue(state[FACING].rotateYClockwise())
        }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(FACING, mirror.apply(state[FACING]))

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    fun sideCoversSmallSquare(state: BlockState): Boolean =
        state[SHAPE] != Shape.STRAIGHT

    private fun connectsTo(state: BlockState, direction: Direction): Boolean {
        if (!direction.axis.isHorizontal) {
            return false
        }

        val facing = state[FACING]

        return when (state[SHAPE]!!) {
            Shape.STRAIGHT -> facing.axis != direction.axis
            Shape.CLOCKWISE_CORNER -> direction == facing.rotateYCounterclockwise() || direction == facing.opposite
            Shape.COUNTERCLOCKWISE_CORNER -> direction == facing.rotateYClockwise() || direction == facing.opposite
            Shape.CLOCKWISE_INNER_CORNER -> direction == facing.rotateYClockwise() || direction == facing
            Shape.COUNTERCLOCKWISE_INNER_CORNER -> direction == facing.rotateYCounterclockwise() || direction == facing
        }
    }

    companion object {
        val SHAPE: EnumProperty<Shape> = EnumProperty.of("shape", Shape::class.java)
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        private val STRAIGHT_OUTLINE_SHAPES = buildShapeRotationsFromNorth(0, 0, 7, 16, 16, 9)
        private val CORNER_OUTLINE_SHAPES = mergeIntoShapeMap(
            mergeShapeMaps(
                buildShapeRotationsFromNorth(0, 0, 7, 9, 16, 9),
                buildShapeRotationsFromNorth(7, 0, 9, 9, 16, 16)
            ),
            PostBlock.Y_SHAPE
        )
        private val STRAIGHT_COLLISION_SHAPES = buildShapeRotationsFromNorth(0, 0, 7, 16, 24, 9)
        private val CORNER_COLLISION_SHAPES = mergeIntoShapeMap(
            mergeShapeMaps(
                buildShapeRotationsFromNorth(0, 0, 7, 9, 24, 9),
                buildShapeRotationsFromNorth(7, 0, 9, 9, 24, 16)
            ),
            createCuboidShape(6.0, 0.0, 6.0, 10.0, 24.0, 10.0)
        )
    }

    enum class Shape(private val id: String) : StringIdentifiable {
        STRAIGHT("straight"),
        CLOCKWISE_CORNER("clockwise_corner"),
        COUNTERCLOCKWISE_CORNER("counterclockwise_corner"),
        CLOCKWISE_INNER_CORNER("clockwise_inner_corner"),
        COUNTERCLOCKWISE_INNER_CORNER("counterclockwise_inner_corner")
        ;

        override fun asString() = id
    }
}
