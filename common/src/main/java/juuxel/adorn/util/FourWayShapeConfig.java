package juuxel.adorn.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record FourWayShapeConfig(VoxelShape baseShape, VoxelShape cornerX0Z0, VoxelShape cornerX1Z0, VoxelShape cornerX0Z1, VoxelShape cornerX1Z1, Map<Direction, VoxelShape> edges) {
    public FourWayShapeConfig(VoxelShape baseShape, VoxelShape cornerX0Z0, VoxelShape cornerX1Z0, VoxelShape cornerX0Z1, VoxelShape cornerX1Z1) {
        this(baseShape, cornerX0Z0, cornerX1Z0, cornerX0Z1, cornerX1Z1, Map.of());
    }

    public FourWayShapeConfig addToBaseShape(VoxelShape shape) {
        return new FourWayShapeConfig(Shapes.or(baseShape, shape), cornerX0Z0, cornerX1Z0, cornerX0Z1, cornerX1Z1);
    }

    public VoxelShape makeShape(boolean north, boolean east, boolean south, boolean west) {
        List<VoxelShape> parts = new ArrayList<>();
        parts.add(baseShape);

        if (north || east || south || west) {
            var trueCount = 0;
            if (north) trueCount++;
            if (east) trueCount++;
            if (south) trueCount++;
            if (west) trueCount++;

            if (trueCount == 2) {
                // Corners
                if (north && west) {
                    parts.add(cornerX1Z1);
                } else if (north && east) {
                    parts.add(cornerX0Z1);
                } else if (south && west) {
                    parts.add(cornerX1Z0);
                } else if (south && east) {
                    parts.add(cornerX0Z0);
                }
            } else if (trueCount == 1) {
                // Ends
                if (north) {
                    parts.add(cornerX0Z1);
                    parts.add(cornerX1Z1);
                } else if (south) {
                    parts.add(cornerX0Z0);
                    parts.add(cornerX1Z0);
                } else if (east) {
                    parts.add(cornerX0Z0);
                    parts.add(cornerX0Z1);
                } else {
                    parts.add(cornerX1Z0);
                    parts.add(cornerX1Z1);
                }
            }
        } else {
            // No connections = all corners
            parts.add(cornerX0Z0);
            parts.add(cornerX1Z0);
            parts.add(cornerX0Z1);
            parts.add(cornerX1Z1);
        }

        if (!north) {
            var shape = edges.get(Direction.NORTH);
            if (shape != null) parts.add(shape);
        }

        if (!east) {
            var shape = edges.get(Direction.EAST);
            if (shape != null) parts.add(shape);
        }

        if (!south) {
            var shape = edges.get(Direction.SOUTH);
            if (shape != null) parts.add(shape);
        }

        if (!west) {
            var shape = edges.get(Direction.WEST);
            if (shape != null) parts.add(shape);
        }

        return parts.stream().reduce(Shapes::or).get();
    }
}
