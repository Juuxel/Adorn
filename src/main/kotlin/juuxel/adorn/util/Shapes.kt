package juuxel.adorn.util

import net.minecraft.block.Block.createCuboidShape
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import java.util.*

/**
 * Creates a map of horizontal VoxelShape rotations from the provided coordinates for **east**.
 */
fun buildShapeRotations(x0: Int, y0: Int, z0: Int, x1: Int, y1: Int, z1: Int): Map<Direction, VoxelShape> = EnumMap(mapOf(
    Direction.EAST to createCuboidShape(
        x0.toDouble(), y0.toDouble(), z0.toDouble(),
        x1.toDouble(), y1.toDouble(), z1.toDouble()
    ),

    Direction.WEST to createCuboidShape(
        16.0 - x1, y0.toDouble(), 16.0 - z1,
        16.0 - x0, y1.toDouble(), 16.0 - z0
    ),

    Direction.SOUTH to createCuboidShape(
        16.0 - z1, y0.toDouble(), x0.toDouble(),
        16.0 - z0, y1.toDouble(), x1.toDouble()
    ),

    Direction.NORTH to createCuboidShape(
        z1.toDouble(), y0.toDouble(), 16.0 - x0,
        z0.toDouble(), y1.toDouble(), 16.0 - x1
    )
))
