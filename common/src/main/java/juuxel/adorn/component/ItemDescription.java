package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public record ItemDescription(Text description) implements TooltipAppender {
    public static final Codec<ItemDescription> CODEC = TextCodecs.CODEC.xmap(ItemDescription::new, ItemDescription::description);

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(applyDescriptionStyle(description.copy()));
    }

    public static ItemDescription ofTranslation(String translationKey) {
        return new ItemDescription(Text.translatable(translationKey));
    }

    public static MutableText applyDescriptionStyle(MutableText text) {
        return text.styled(style -> style.withItalic(true).withColor(Formatting.DARK_GRAY));
    }
}
