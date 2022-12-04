package juuxel.adorn.block

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.util.buildShapeRotationsFromNorth
import juuxel.adorn.util.getDirection
import juuxel.adorn.util.turnHorizontally
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.fluid.FluidState
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
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class BenchBlock(variant: BlockVariant) : SeatBlock(variant.createSettings()), Waterloggable, BlockWithDescription {
    override val sittingStat = AdornStats.SIT_ON_BENCH
    override val descriptionKey = "block.adorn.bench.description"

    init {
        defaultState = defaultState
            .with(AXIS, Direction.Axis.Z)
            .with(CONNECTED_N, false)
            .with(CONNECTED_P, false)
            .with(WATERLOGGED, false)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState
            .with(AXIS, ctx.playerFacing.axis.turnHorizontally())
            .with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid == Fluids.WATER)
            .let { state -> updateConnections(ctx.world, ctx.blockPos, state) }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return updateConnections(world, pos, state)
    }

    private fun updateConnections(world: BlockView, pos: BlockPos, state: BlockState): BlockState =
        Direction.AxisDirection.values().fold(state) { acc, axisDirection ->
            val property = if (axisDirection == Direction.AxisDirection.NEGATIVE) CONNECTED_N else CONNECTED_P
            val neighbor = world.getBlockState(pos.offset(state[AXIS], axisDirection.offset()))
            val connected = neighbor.block is BenchBlock && neighbor[AXIS] == state[AXIS]

            acc.with(property, connected)
        }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, ctx: ShapeContext): VoxelShape =
        SHAPES[Bits.buildBenchState(state[AXIS], state[CONNECTED_N], state[CONNECTED_P])]

    @Suppress("DEPRECATION")
    override fun getFluidState(state: BlockState): FluidState =
        if (state[Properties.WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(AXIS, CONNECTED_N, CONNECTED_P, WATERLOGGED)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        if (rotation == BlockRotation.COUNTERCLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90) {
            state.with(AXIS, state[AXIS].turnHorizontally())
        } else {
            state
        }

    companion object {
        val AXIS: EnumProperty<Direction.Axis> = Properties.HORIZONTAL_AXIS
        val CONNECTED_N: BooleanProperty = BooleanProperty.of("connected_n")
        val CONNECTED_P: BooleanProperty = BooleanProperty.of("connected_p")
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        private val X_TOP_SHAPE: VoxelShape = createCuboidShape(0.0, 8.0, 1.0, 16.0, 10.0, 15.0)
        private val Z_TOP_SHAPE: VoxelShape = createCuboidShape(1.0, 8.0, 0.0, 15.0, 10.0, 16.0)
        private val SHAPES: Byte2ObjectMap<VoxelShape> = Byte2ObjectArrayMap()

        init {
            val legShapes = buildShapeRotationsFromNorth(2, 0, 2, 14, 8, 4)
            val booleans = booleanArrayOf(true, false)

            for (axis in arrayOf(Direction.Axis.X, Direction.Axis.Z)) {
                val topShape = if (axis == Direction.Axis.X) X_TOP_SHAPE else Z_TOP_SHAPE
                val negativeLeg = legShapes[axis.getDirection(Direction.AxisDirection.NEGATIVE)]!!
                val positiveLeg = legShapes[axis.getDirection(Direction.AxisDirection.POSITIVE)]!!

                for (connectedN in booleans) {
                    for (connectedP in booleans) {
                        val parts = ArrayList<VoxelShape>()

                        if (!connectedN) {
                            parts += negativeLeg
                        }

                        if (!connectedP) {
                            parts += positiveLeg
                        }

                        val key = Bits.buildBenchState(axis, connectedN, connectedP)
                        SHAPES[key] = VoxelShapes.union(topShape, *parts.toTypedArray())
                    }
                }
            }
        }
    }
}
