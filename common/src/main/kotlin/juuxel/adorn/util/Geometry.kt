package juuxel.adorn.util

import org.joml.Vector3d

object Geometry {
    fun isPointInsideQuadrilateral(
        x: Double, y: Double, z: Double,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        x3: Double, y3: Double, z3: Double,
        x4: Double, y4: Double, z4: Double
    ): Boolean {
        // https://stackoverflow.com/a/16260220/19096973
        val buffer = Vector3d()
        val quadrilateralArea = triangleArea(buffer, x1, y1, z1, x2, y2, z2, x3, y3, z3) +
            triangleArea(buffer, x4, y4, z4, x2, y2, z2, x3, y3, z3)
        val triangleAreas = triangleArea(buffer, x, y, z, x1, y1, z1, x2, y2, z2) +
            triangleArea(buffer, x, y, z, x2, y2, z2, x3, y3, z3) +
            triangleArea(buffer, x, y, z, x3, y3, z3, x4, y4, z4) +
            triangleArea(buffer, x, y, z, x4, y4, z4, x1, y1, z1)
        return triangleAreas - quadrilateralArea <= 0.01 // allow a little imprecision due to floating point errors
    }

    private fun triangleArea(
        buffer: Vector3d,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        x3: Double, y3: Double, z3: Double
    ): Double =
        buffer.set(x2 - x1, y2 - y1, z2 - z1)
            .cross(x3 - x1, y3 - y1, z3 - z1)
            .length()
}
