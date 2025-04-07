package juuxel.adorn.item;

import juuxel.adorn.block.BlockWithDescription;
import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class AdornTallBlockItem extends TallBlockItem {
    public AdornTallBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        if (getBlock() instanceof BlockWithDescription withDescription) {
            textConsumer.accept(ItemWithDescription.createDescriptionText(withDescription.getDescriptionKey()));
        }
    }
}
