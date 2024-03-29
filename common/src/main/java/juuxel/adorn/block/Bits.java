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

    static byte buildTableState(boolean north, boolean east, boolean south, boolean west, boolean hasCarpet) {
        return (byte) ((north ? 1 : 0) << 4 | (east ? 1 : 0) << 3 | (south ? 1 : 0) << 2 | (west ? 1 : 0) << 1 | (hasCarpet ? 1 : 0));
    }

    static byte buildBenchState(Direction.Axis axis, boolean connectedN, boolean connectedP) {
        return (byte) ((axis == Direction.Axis.X ? 1 : 0) << 2 | (connectedN ? 1 : 0) << 1 | (connectedP ? 1 : 0));
    }

    static byte buildCopperPipeState(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down) {
        int northB = north ? 1 : 0;
        int eastB = east ? 1 : 0;
        int southB = south ? 1 : 0;
        int westB = west ? 1 : 0;
        int upB = up ? 1 : 0;
        int downB = down ? 1 : 0;

        return (byte) (northB << 5 | eastB << 4 | southB << 3 | westB << 2 | upB << 1 | downB);
    }
}
