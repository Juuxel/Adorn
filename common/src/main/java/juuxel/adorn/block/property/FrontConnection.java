package juuxel.adorn.block.property;

import net.minecraft.util.StringRepresentable;

/**
 * Note: sofas have left and right flipped
 */
public enum FrontConnection implements StringRepresentable {
    /**
     * No connection to the block in front.
     */
    NONE("none"),

    /**
     * The block will be connected to the block in front on its left side (looking from the connecting block).
     */
    LEFT("left"),

    /**
     * The block will be connected to the block in front on its right side (looking from the connecting block).
     */
    RIGHT("right");

    private final String id;

    FrontConnection(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return id;
    }
}
