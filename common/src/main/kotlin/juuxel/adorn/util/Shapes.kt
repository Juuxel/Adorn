package juuxel.adorn.util

import net.minecraft.block.Block.createCuboidShape
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.EnumMap

/**
 * Creates a map of horizontal cuboid VoxelShape rotations from the provided coordinates for **east**.
 *
 * The coordinates are specified like in a model json or [createCuboidShape]:
 * { from: ([x0], [y0], [z0]), to: ([x1], [y1], [z1]) }
 */
fun buildShapeRotations(x0: Int, y0: Int, z0: Int, x1: Int, y1: Int, z1: Int): MutableMap<Direction, VoxelShape> =
    enumMapOf(
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
            z0.toDouble(), y0.toDouble(), 16.0 - x1,
            z1.toDouble(), y1.toDouble(), 16.0 - x0
        )
    )

/**
 * Creates a map of horizontal cuboid VoxelShape rotations from the provided coordinates for **north**.
 *
 * The coordinates are specified like in a model json or [createCuboidShape]:
 * { from: ([x0], [y0], [z0]), to: ([x1], [y1], [z1]) }
 */
fun buildShapeRotationsFromNorth(x0: Int, y0: Int, z0: Int, x1: Int, y1: Int, z1: Int): MutableMap<Direction, VoxelShape> =
    enumMapOf(
        Direction.NORTH to createCuboidShape(
            x0.toDouble(), y0.toDouble(), z0.toDouble(),
            x1.toDouble(), y1.toDouble(), z1.toDouble()
        ),

        Direction.SOUTH to createCuboidShape(
            16.0 - x1, y0.toDouble(), 16.0 - z1,
            16.0 - x0, y1.toDouble(), 16.0 - z0
        ),

        Direction.EAST to createCuboidShape(
            16.0 - z1, y0.toDouble(), x0.toDouble(),
            16.0 - z0, y1.toDouble(), x1.toDouble()
        ),

        Direction.WEST to createCuboidShape(
            z0.toDouble(), y0.toDouble(), 16.0 - x1,
            z1.toDouble(), y1.toDouble(), 16.0 - x0
        )
    )

/**
 * Merges the [shape maps][maps] together.
 */
fun mergeShapeMaps(vararg maps: Map<Direction, VoxelShape>): Map<Direction, VoxelShape> {
    fun Map<Direction, VoxelShape>.getShape(direction: Direction) =
        getOrElse(direction) { throw IllegalArgumentException("Map is missing shape for $direction!") }

    return EnumMap(
        Direction.Type.HORIZONTAL.associateWith { direction ->
            maps.map { it.getShape(direction) }.reduce { a, b -> VoxelShapes.union(a, b) }.simplify()
        }
    )
}

/**
 * Merges the [shape] into the [shape map][map].
 */
fun mergeIntoShapeMap(map: Map<Direction, VoxelShape>, shape: VoxelShape): Map<Direction, VoxelShape> =
    EnumMap(map.mapValues { (_, it) -> VoxelShapes.union(it, shape) })
