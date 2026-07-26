package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

public record ItemDescription(
    Component description) implements TooltipProvider {
    public static final Codec<ItemDescription> CODEC = ComponentSerialization.CODEC.xmap(ItemDescription::new, ItemDescription::description);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(applyDescriptionStyle(description.copy()));
    }

    public static ItemDescription ofTranslation(String translationKey) {
        return new ItemDescription(Component.translatable(translationKey));
    }

    public static ItemDescription ofItem(Identifier id) {
        return ofTranslation("item." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public static ItemDescription ofBlock(Identifier id) {
        return ofTranslation("block." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public static ItemDescription ofEntity(Identifier id) {
        return ofTranslation("entity." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public static MutableComponent applyDescriptionStyle(MutableComponent text) {
        return text.withStyle(style -> style.withItalic(true).withColor(ChatFormatting.DARK_GRAY));
    }
}
