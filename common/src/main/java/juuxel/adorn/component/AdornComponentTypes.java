package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import juuxel.adorn.item.WateringCanItem;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.trading.TradeOwner;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.core.registries.Registries;

import java.util.List;
import java.util.function.UnaryOperator;

public final class AdornComponentTypes {
    public static final Registrar<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistrarFactory.get().create(Registries.DATA_COMPONENT_TYPE);

    public static final Registered<DataComponentType<Trade>> TRADE = register("trade", builder -> builder.persistent(Trade.CODEC));
    public static final Registered<DataComponentType<TradeOwner>> TRADE_OWNER = register("trade_owner", builder -> builder.persistent(TradeOwner.CODEC));
    public static final Registered<DataComponentType<Integer>> WATER_LEVEL = register("water_level", builder -> builder.persistent(Codec.INT));
    public static final Registered<DataComponentType<WateringCanItem.FertilizerLevel>> FERTILIZER_LEVEL = register("fertilizer_level", builder -> builder.persistent(WateringCanItem.FertilizerLevel.CODEC));
    public static final Registered<DataComponentType<ItemDescription>> DESCRIPTION = register("description", builder -> builder.persistent(ItemDescription.CODEC));
    public static final Registered<DataComponentType<ConeVariantComponent>> CONE_VARIANT = register("cone_variant",
        builder -> builder.persistent(ConeVariantComponent.CODEC).networkSynchronized(ConeVariantComponent.PACKET_CODEC)
    );

    public static void init() {
    }

    private static <T> Registered<DataComponentType<T>> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(id, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static List<Registered<? extends DataComponentType<? extends TooltipProvider>>> getTooltipComponents() {
        return List.of(
            TRADE_OWNER,
            FERTILIZER_LEVEL,
            DESCRIPTION
        );
    }
}
