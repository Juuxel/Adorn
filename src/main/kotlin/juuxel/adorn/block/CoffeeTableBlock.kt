package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

open class CoffeeTableBlock(variant: BlockVariant) : AbstractTableBlock(variant.createSettings().nonOpaque()) {
    override fun canConnectTo(state: BlockState, sideOfSelf: Direction) =
        state.block is CoffeeTableBlock

    override fun getShapeForKey(key: Byte): VoxelShape = SHAPE

    override fun isCarpetingEnabled() = false

    override fun isSittingEnabled() = false

    override fun isSimpleFullBlock(state: BlockState?, view: BlockView?, pos: BlockPos?) = false

    companion object {

        private val SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)
        //private val SHAPES: Byte2ObjectMap<VoxelShape>

        init {
            /*val topShape = createCuboidShape(0.0, 10.0, 0.0, 16.0, 12.0, 16.0)
            val legX0Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 10.0, 4.0)
            val legX1Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 10.0, 4.0)
            val legX0Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 10.0, 15.0)
            val legX1Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 10.0, 15.0)
            val booleans = setOf(true, false)

            fun makeShape(
                north: Boolean,
                east: Boolean,
                south: Boolean,
                west: Boolean
            ): VoxelShape {
                val parts = arrayListOf<VoxelShape?>(topShape)

                if (north || east || south || west) {
                    val trueCount = booleanArrayOf(north, east, south, west).count { it }

                    if (trueCount == 2) {
                        // Corners
                        parts += when {
                            north && west -> legX1Z1
                            north && east -> legX0Z1
                            south && west -> legX1Z0
                            south && east -> legX0Z0
                            else -> null // Straight lines ignored
                        }
                    } else if (trueCount == 1) {
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

                return parts.filterNotNull().reduce(VoxelShapes::union)
            }

            SHAPES = Byte2ObjectOpenHashMap(run {
                booleans.flatMap { north ->
                    booleans.flatMap { east ->
                        booleans.flatMap { south ->
                            booleans.map { west ->
                                Bits.buildCoffeeTableState(north, east, south, west) to
                                    makeShape(north, east, south, west)
                            }
                        }
                    }
                }.toMap()
            })*/

        }
    }
}
