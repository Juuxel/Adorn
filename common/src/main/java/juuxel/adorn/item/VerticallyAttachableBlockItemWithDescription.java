package juuxel.adorn.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

import java.util.List;

public class VerticallyAttachableBlockItemWithDescription extends VerticallyAttachableBlockItem {
    public VerticallyAttachableBlockItemWithDescription(Block standingBlock, Block wallBlock, Settings settings, Direction verticalAttachmentDirection) {
        super(standingBlock, wallBlock, settings, verticalAttachmentDirection);
    }

    protected String getDescriptionKey(ItemStack stack) {
        return getTranslationKey(stack) + ".description";
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(ItemWithDescription.createDescriptionText(getDescriptionKey(stack)));
    }
}
