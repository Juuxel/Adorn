package juuxel.adorn.item;

import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

import java.util.function.Consumer;

@Deprecated
public final class VerticallyAttachableBlockItemWithDescription extends VerticallyAttachableBlockItem {
    public VerticallyAttachableBlockItemWithDescription(Block standingBlock, Block wallBlock, Direction verticalAttachmentDirection, Settings settings) {
        super(standingBlock, wallBlock, verticalAttachmentDirection, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(ItemWithDescription.createDescriptionText(getTranslationKey() + ".description"));
    }
}
