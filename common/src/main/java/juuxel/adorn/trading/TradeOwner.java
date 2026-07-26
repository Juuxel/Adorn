package juuxel.adorn.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;
import java.util.function.Consumer;

public record TradeOwner(UUID uuid, Component name) implements TooltipProvider {
    private static final String DESCRIPTION_TRANSLATION_KEY = "block.adorn.trading_station.description.owner";

    public static final Codec<TradeOwner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.AUTHLIB_CODEC.fieldOf("uuid").forGetter(TradeOwner::uuid),
        ComponentSerialization.CODEC.fieldOf("name").forGetter(TradeOwner::name)
    ).apply(instance, TradeOwner::new));

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable(DESCRIPTION_TRANSLATION_KEY, name.copy().withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
    }
}
