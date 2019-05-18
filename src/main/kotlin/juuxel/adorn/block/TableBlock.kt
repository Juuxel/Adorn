package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
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

class TableBlock(material: String) : CarpetedBlock(Settings.copy(Blocks.CRAFTING_TABLE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_table"
    override val itemSettings: Nothing? = null
    override val sittingYOffset = 0.6

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
        SHAPES[Bits.buildTableState(state[NORTH], state[EAST], state[SOUTH], state[WEST], state[CARPET].isPresent)]

    companion object {
        val NORTH = BooleanProperty.create("north")
        val EAST = BooleanProperty.create("east")
        val SOUTH = BooleanProperty.create("south")
        val WEST = BooleanProperty.create("west")
        val CARPET = CarpetedBlock.CARPET

        private val SHAPES: Byte2ObjectMap<VoxelShape>

        init {
            val topShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
            val legX0Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0)
            val legX1Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0)
            val legX0Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0)
            val legX1Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
            val booleans = setOf(true, false)

            fun makeShape(
                north: Boolean,
                east: Boolean,
                south: Boolean,
                west: Boolean,
                hasCarpet: Boolean
            ): VoxelShape {
                val parts = arrayListOf<VoxelShape?>(topShape)

                if (north || east || south || west) {
                    val trueCount = booleanArrayOf(north, east, south, west).count { it }
                    if (trueCount >= 3) return topShape
                    if (north && south && !west && !east) return topShape
                    if (!north && !south && west && east) return topShape

                    if (trueCount == 2) {
                        // Corners
                        parts += when {
                            north && west -> legX1Z1
                            north && east -> legX0Z1
                            south && west -> legX1Z0
                            south && east -> legX0Z0
                            else -> null
                        }
                    } else {
                        // Ends
                        when {
                            north -> {
                                parts += legX0Z1
                                parts += legX1Z1
                            }

                            south -> {
                                parts += legX0Z0
                                parts += legX1Z0
                            }

                            east -> {
                                parts += legX0Z0
                                parts += legX0Z1
                            }

                            west -> {
                                parts += legX1Z0
                                parts += legX1Z1
                            }
                        }
                    }
                } else {
                    // No connections = all legs
                    parts += legX0Z0
                    parts += legX1Z0
                    parts += legX0Z1
                    parts += legX1Z1
                }

                if (hasCarpet) {
                    parts += CARPET_SHAPE
                }

                return parts.filterNotNull().reduce(VoxelShapes::union)
            }

            SHAPES = Byte2ObjectOpenHashMap(run {
                booleans.flatMap { north ->
                    booleans.flatMap { east ->
                        booleans.flatMap { south ->
                            booleans.flatMap { west ->
                                booleans.map { hasCarpet ->
                                    Bits.buildTableState(north, east, south, west, hasCarpet) to
                                            makeShape(north, east, south, west, hasCarpet)
                                }
                            }
                        }
                    }
                }.toMap()
            })

        }
    }
}
