package juuxel.adorn.item;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public class ItemWithDescription extends Item {
    public ItemWithDescription(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(createDescriptionText(getTranslationKey() + ".description"));
    }

    public static Text createDescriptionText(String translationKey) {
        return Text.translatable(translationKey)
            .styled(style -> style.withItalic(true).withColor(Formatting.DARK_GRAY));
    }
}
