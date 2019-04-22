package juuxel.adorn.util

import net.minecraft.util.Rotation
import net.minecraft.util.math.BoundingBox
import net.minecraft.util.math.Direction

fun BoundingBox.rotate(axis: Direction.Axis, rotation: Rotation): BoundingBox {
    if (rotation == Rotation.ROT_0) return this

    return when (axis) {
        Direction.Axis.X -> {
            when (rotation) {
                Rotation.ROT_90 -> BoundingBox(minX, maxX, minZ, maxZ, maxY, minY)
                Rotation.ROT_180 -> BoundingBox(minX, maxX, maxY, minY, maxZ, minZ)
                Rotation.ROT_270 -> rotate(axis, Rotation.ROT_90).rotate(axis, Rotation.ROT_180)
                else -> this
            }
        }

        Direction.Axis.Y -> {
            when (rotation) {
                Rotation.ROT_90 -> BoundingBox(maxZ, minZ, minY, maxY, minX, maxX)
                Rotation.ROT_180 -> BoundingBox(maxX, minX, minY, maxY, maxZ, minZ)
                Rotation.ROT_270 -> rotate(axis, Rotation.ROT_90).rotate(axis, Rotation.ROT_180)
                else -> this
            }
        }

        Direction.Axis.Z -> {
            when (rotation) {
                Rotation.ROT_90 -> BoundingBox(minY, maxY, maxX, minX, minZ, maxZ)
                Rotation.ROT_180 -> BoundingBox(maxX, minX, maxY, minY, minZ, maxZ)
                Rotation.ROT_270 -> rotate(axis, Rotation.ROT_90).rotate(axis, Rotation.ROT_180)
                else -> this
            }
        }
    }
}

fun BoundingBox.rotateYClockwise(): BoundingBox =
    BoundingBox(maxX, minZ, minY, maxY, minX, minZ)

fun BoundingBox.rotateYCounterclockwise(): BoundingBox =
    BoundingBox(minZ, minX, minY, maxY, maxZ, maxX)

fun BoundingBox.flipY(): BoundingBox =
    BoundingBox(maxX, minX, minY, maxY, maxZ, minZ)
