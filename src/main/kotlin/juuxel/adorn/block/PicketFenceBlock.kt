package juuxel.adorn.block

import juuxel.adorn.util.buildShapeRotationsFromNorth
import juuxel.adorn.util.mergeIntoShapeMap
import juuxel.adorn.util.mergeShapeMaps
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
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

class PicketFenceBlock(settings: Settings) : Block(settings), Waterloggable {
    init {
        defaultState = defaultState
            .with(FACING, Direction.NORTH)
            .with(SHAPE, Shape.Straight)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(SHAPE, FACING, WATERLOGGED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)?.let { state ->
            state.with(FACING, ctx.playerFacing.opposite)
                .with(SHAPE, Shape.Straight)
                .with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER)
                .let {
                    val side = it[FACING].opposite
                    val pos = ctx.blockPos.offset(side)
                    val world = ctx.world
                    it.getStateForNeighborUpdate(side, world.getBlockState(pos), world, ctx.blockPos, pos)
                }
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, facing: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        val fenceFacing = state[FACING]
        if (facing == fenceFacing.opposite) {
            val neighborFacing = if (neighborState.block is PicketFenceBlock) neighborState[FACING] else null
            val neighborShape = if (neighborState.block is PicketFenceBlock) neighborState[SHAPE] else null

            var shape = when (neighborFacing) {
                fenceFacing.rotateYClockwise() -> Shape.ClockwiseCorner
                fenceFacing.rotateYCounterclockwise() -> Shape.CounterclockwiseCorner
                else -> Shape.Straight
            }

            // Prevent funny connections
            if (neighborShape != shape && neighborShape != Shape.Straight)
                shape = Shape.Straight

            return state.with(SHAPE, shape)
        }
        return state
    }

    override fun getOutlineShape(
        state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape = when (state[SHAPE]) {
        Shape.Straight -> STRAIGHT_OUTLINE_SHAPES.getValue(state[FACING])
        Shape.ClockwiseCorner -> CORNER_OUTLINE_SHAPES.getValue(state[FACING])
        Shape.CounterclockwiseCorner -> CORNER_OUTLINE_SHAPES.getValue(state[FACING].rotateYCounterclockwise())
    }

    override fun getCollisionShape(
        state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape = when (state[SHAPE]) {
        Shape.Straight -> STRAIGHT_COLLISION_SHAPES.getValue(state[FACING])
        Shape.ClockwiseCorner -> CORNER_COLLISION_SHAPES.getValue(state[FACING])
        Shape.CounterclockwiseCorner -> CORNER_COLLISION_SHAPES.getValue(state[FACING].rotateYCounterclockwise())
    }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.with(FACING, mirror.apply(state[FACING]))

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
        Straight("straight"),
        ClockwiseCorner("clockwise_corner"),
        CounterclockwiseCorner("counterclockwise_corner"),
        ;

        override fun asString() = id
    }
}
