package juuxel.adorn.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public record TradeOwner(UUID uuid, Text name) implements TooltipAppender {
    private static final String DESCRIPTION_TRANSLATION_KEY = "block.adorn.trading_station.description.owner";

    public static final Codec<TradeOwner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Uuids.CODEC.fieldOf("uuid").forGetter(TradeOwner::uuid),
        TextCodecs.CODEC.fieldOf("name").forGetter(TradeOwner::name)
    ).apply(instance, TradeOwner::new));

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, @Nullable PlayerEntity player, ItemStack stack) {
        textConsumer.accept(Text.translatable(DESCRIPTION_TRANSLATION_KEY, name.copy().formatted(Formatting.WHITE)).formatted(Formatting.GREEN));
    }
}
