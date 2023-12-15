@file:Suppress("OVERRIDE_DEPRECATION")

package juuxel.adorn.block

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.util.buildShapeRotationsFromNorth
import juuxel.adorn.util.enumMapOf
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.FluidState
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

open class CopperPipeBlock(settings: Settings) : Block(settings), Waterloggable, BlockWithDescription {
    override val descriptionKey = "block.adorn.copper_pipe.description"

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        SHAPES.get(Bits.buildCopperPipeState(state[NORTH], state[EAST], state[SOUTH], state[WEST], state[UP], state[DOWN]))

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        var state = defaultState.with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER)

        for (direction in Direction.entries) {
            state = updateConnection(state, ctx.world.getBlockState(ctx.blockPos.offset(direction)), direction)
        }

        return state
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return updateConnection(state, neighborState, direction)
    }

    private fun updateConnection(state: BlockState, neighborState: BlockState, direction: Direction): BlockState =
        state.with(CONNECTION_PROPERTIES[direction], shouldConnectTo(neighborState))

    override fun getFluidState(state: BlockState): FluidState =
        if (state[Properties.WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN)
        builder.add(WATERLOGGED)
    }

    companion object {
        val NORTH: BooleanProperty = Properties.NORTH
        val EAST: BooleanProperty = Properties.EAST
        val SOUTH: BooleanProperty = Properties.SOUTH
        val WEST: BooleanProperty = Properties.WEST
        val UP: BooleanProperty = Properties.UP
        val DOWN: BooleanProperty = Properties.DOWN
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        private val CONNECTION_PROPERTIES = enumMapOf(
            Direction.NORTH to NORTH,
            Direction.EAST to EAST,
            Direction.SOUTH to SOUTH,
            Direction.WEST to WEST,
            Direction.UP to UP,
            Direction.DOWN to DOWN
        )

        private val SHAPES: Byte2ObjectMap<VoxelShape> = Byte2ObjectOpenHashMap()

        init {
            val center = createCuboidShape(6.0, 6.0, 6.0, 10.0, 10.0, 10.0)
            val pipes = buildShapeRotationsFromNorth(7, 7, 0, 9, 9, 8)
            pipes[Direction.UP] = createCuboidShape(7.0, 8.0, 7.0, 9.0, 16.0, 9.0)
            pipes[Direction.DOWN] = createCuboidShape(7.0, 0.0, 7.0, 9.0, 8.0, 9.0)
            val ringX = createCuboidShape(7.0, 6.0, 6.0, 9.0, 10.0, 10.0)
            val ringY = createCuboidShape(6.0, 7.0, 6.0, 10.0, 9.0, 10.0)
            val ringZ = createCuboidShape(6.0, 6.0, 7.0, 10.0, 10.0, 9.0)

            val booleans = arrayOf(true, false)
            for (north in booleans) {
                for (east in booleans) {
                    for (south in booleans) {
                        for (west in booleans) {
                            for (up in booleans) {
                                for (down in booleans) {
                                    // If it spans multiple axes or only has one connection along an axis or if it has no connections
                                    val axes = HashSet<Direction.Axis>()
                                    if (west || east) {
                                        axes += Direction.Axis.X
                                    }
                                    if (up || down) {
                                        axes += Direction.Axis.Y
                                    }
                                    if (north || south) {
                                        axes += Direction.Axis.Z
                                    }

                                    val hasCenter = (!north && !east && !south && !west && !up && !down) ||
                                        (north && !south) ||
                                        (east && !west) ||
                                        (south && !north) ||
                                        (west && !east) ||
                                        (up && !down) ||
                                        (down && !up) ||
                                        axes.size > 1

                                    var shape = if (hasCenter) {
                                        center
                                    } else if (east) { // Straight pipe along X axis
                                        ringX
                                    } else if (up) { // Straight pipe along Y axis
                                        ringY
                                    } else { // Straight pipe along Z axis
                                        ringZ
                                    }

                                    if (north) shape = VoxelShapes.union(shape, pipes[Direction.NORTH])
                                    if (east) shape = VoxelShapes.union(shape, pipes[Direction.EAST])
                                    if (south) shape = VoxelShapes.union(shape, pipes[Direction.SOUTH])
                                    if (west) shape = VoxelShapes.union(shape, pipes[Direction.WEST])
                                    if (up) shape = VoxelShapes.union(shape, pipes[Direction.UP])
                                    if (down) shape = VoxelShapes.union(shape, pipes[Direction.DOWN])

                                    SHAPES.put(Bits.buildCopperPipeState(north, east, south, west, up, down), shape)
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun shouldConnectTo(state: BlockState): Boolean =
            state.isIn(AdornTags.COPPER_PIPES_CONNECT_TO)
    }
}
