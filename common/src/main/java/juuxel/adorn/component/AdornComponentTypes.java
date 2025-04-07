package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import juuxel.adorn.item.WateringCanItem;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.trading.TradeOwner;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.UnaryOperator;

public final class AdornComponentTypes {
    public static final Registrar<ComponentType<?>> DATA_COMPONENT_TYPES = RegistrarFactory.get().create(RegistryKeys.DATA_COMPONENT_TYPE);

    public static final Registered<ComponentType<Trade>> TRADE = register("trade", builder -> builder.codec(Trade.CODEC));
    public static final Registered<ComponentType<TradeOwner>> TRADE_OWNER = register("trade_owner", builder -> builder.codec(TradeOwner.CODEC));
    public static final Registered<ComponentType<Integer>> WATER_LEVEL = register("water_level", builder -> builder.codec(Codec.INT));
    public static final Registered<ComponentType<WateringCanItem.FertilizerLevel>> FERTILIZER_LEVEL = register("fertilizer_level", builder -> builder.codec(WateringCanItem.FertilizerLevel.CODEC));

    public static void init() {
    }

    private static <T> Registered<ComponentType<T>> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(id, () -> builderOperator.apply(ComponentType.builder()).build());
    }
}
