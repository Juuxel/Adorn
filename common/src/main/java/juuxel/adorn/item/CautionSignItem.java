package juuxel.adorn.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public final class CautionSignItem extends VerticallyAttachableBlockItemWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.caution_sign.description";

    public CautionSignItem(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings, Direction.DOWN);
    }

    @Override
    protected String getDescriptionKey(ItemStack stack) {
        return DESCRIPTION_KEY;
    }
}
