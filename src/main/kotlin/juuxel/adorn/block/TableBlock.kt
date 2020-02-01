package juuxel.adorn.block

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.config.AdornConfigManager
import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

open class TableBlock(variant: BlockVariant) : AbstractTableBlock(variant.createSettings()) {
    override val sittingYOffset = 0.6

    override fun isSittingEnabled() = AdornConfigManager.CONFIG.sittingOnTables

    override fun canConnectTo(state: BlockState, sideOfSelf: Direction) =
        state.block is TableBlock

    override fun getShapeForKey(key: Byte): VoxelShape = SHAPES[key]

    companion object {
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
