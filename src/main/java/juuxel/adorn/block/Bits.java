package juuxel.adorn.block;

import juuxel.adorn.block.property.FrontConnection;
import net.minecraft.util.math.Direction;

final class Bits {
    private Bits() {
        throw new AssertionError();
    }

    static byte buildSofaState(Direction facing, boolean left, boolean right, FrontConnection front) {
        return (byte) (facing.getHorizontal() << 5 | (left ? 1 : 0) << 3 | (right ? 1 : 0) << 2 | front.ordinal());
    }

    static byte buildTableState(boolean north, boolean east, boolean south, boolean west) {
        return (byte) ((north ? 1 : 0) << 4 | (east ? 1 : 0) << 3 | (south ? 1 : 0) << 2 | (west ? 1 : 0) << 1/* | (hasCarpet ? 1 : 0)*/);
    }
}
