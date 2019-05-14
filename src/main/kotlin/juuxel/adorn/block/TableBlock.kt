package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import virtuoel.towelette.api.Fluidloggable

class TableBlock(material: String) : Block(Settings.copy(Blocks.CRAFTING_TABLE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_table"
    override val itemSettings: Nothing? = null

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(NORTH, EAST, SOUTH, WEST)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        updateConnections(
            super.getPlacementState(context)!!,
            context.world,
            context.blockPos
        )

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: IWorld,
        pos: BlockPos,
        neighborPos: BlockPos
    ) = updateConnections(
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos),
        world,
        pos
    )

    private fun updateConnections(
        state: BlockState,
        world: IWorld,
        pos: BlockPos
    ) = state.with(NORTH, world.getBlockState(pos.offset(Direction.NORTH)).block is TableBlock)
        .with(EAST, world.getBlockState(pos.offset(Direction.EAST)).block is TableBlock)
        .with(SOUTH, world.getBlockState(pos.offset(Direction.SOUTH)).block is TableBlock)
        .with(WEST, world.getBlockState(pos.offset(Direction.WEST)).block is TableBlock)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        SHAPES[TableState(state[NORTH], state[EAST], state[SOUTH], state[WEST])]

    companion object {
        val NORTH = BooleanProperty.create("north")
        val EAST = BooleanProperty.create("east")
        val SOUTH = BooleanProperty.create("south")
        val WEST = BooleanProperty.create("west")

        private val BASE_SHAPE = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        private val LEG_X0_Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0)
        private val LEG_X1_Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0)
        private val LEG_X0_Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0)
        private val LEG_X1_Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)

        private val BOOLEAN_SET = setOf(true, false)
        private val SHAPES: Map<TableState, VoxelShape> = run {
            BOOLEAN_SET.flatMap { north ->
                BOOLEAN_SET.flatMap { east ->
                    BOOLEAN_SET.flatMap { south ->
                        BOOLEAN_SET.map { west ->
                            TableState(north, east, south, west) to makeShape(north, east, south, west)
                        }
                    }
                }
            }.toMap()
        }

        private fun makeShape(north: Boolean, east: Boolean, south: Boolean, west: Boolean): VoxelShape {
            val parts = arrayListOf<VoxelShape?>(BASE_SHAPE)

            if (north || east || south || west) {
                val trueCount = booleanArrayOf(north, east, south, west).count { it }
                if (trueCount >= 3) return BASE_SHAPE
                if (north && south && !west && !east) return BASE_SHAPE
                if (!north && !south && west && east) return BASE_SHAPE

                if (trueCount == 2) {
                    // Corners
                    parts += when {
                        north && west -> LEG_X1_Z1
                        north && east -> LEG_X0_Z1
                        south && west -> LEG_X1_Z0
                        south && east -> LEG_X0_Z0
                        else -> null
                    }
                } else {
                    // Ends
                    when {
                        north -> {
                            parts += LEG_X0_Z1
                            parts += LEG_X1_Z1
                        }

                        south -> {
                            parts += LEG_X0_Z0
                            parts += LEG_X1_Z0
                        }

                        east -> {
                            parts += LEG_X0_Z0
                            parts += LEG_X0_Z1
                        }

                        west -> {
                            parts += LEG_X1_Z0
                            parts += LEG_X1_Z1
                        }
                    }
                }
            } else {
                // No connections = all legs
                parts += LEG_X0_Z0
                parts += LEG_X1_Z0
                parts += LEG_X0_Z1
                parts += LEG_X1_Z1
            }

            return parts.filterNotNull().reduce(VoxelShapes::union)
        }
    }

    private data class TableState(
        val north: Boolean,
        val east: Boolean,
        val south: Boolean,
        val west: Boolean
    )
}
