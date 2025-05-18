package juuxel.adorn.block;

import juuxel.adorn.util.FourWayShapeConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

public final class TableBlock extends AbstractTableBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.table.description";
    private static final VoxelShape[] SHAPES = new VoxelShape[32];

    static {
        var topShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        var legX0Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0);
        var legX1Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0);
        var legX0Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0);
        var legX1Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0);
        FourWayShapeConfig nonCarpetedConfig = new FourWayShapeConfig(topShape, legX0Z0, legX1Z0, legX0Z1, legX1Z1);
        FourWayShapeConfig carpetedConfig = nonCarpetedConfig.addToBaseShape(CARPET_SHAPE);
        var booleans = new boolean[] { true, false };

        for (var north : booleans) {
            for (var east : booleans) {
                for (var south : booleans) {
                    for (var west : booleans) {
                        for (var hasCarpet : booleans) {
                            var key = getShapeKey(north, east, south, west, hasCarpet);
                            var config = hasCarpet ? carpetedConfig : nonCarpetedConfig;
                            var shape = config.makeShape(north, east, south, west);
                            SHAPES[key] = shape;
                        }
                    }
                }
            }
        }
    }

    public TableBlock(Settings settings) {
        super(settings.solid());
    }

    @Override
    public @Nullable Identifier getSittingStat() {
        return null;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected boolean isSittingEnabled() {
        return false;
    }

    @Override
    protected boolean canConnectTo(BlockState state, Direction sideOfSelf) {
        return state.getBlock() instanceof TableBlock;
    }

    @Override
    protected VoxelShape getShapeForKey(int key) {
        return SHAPES[key];
    }
}
