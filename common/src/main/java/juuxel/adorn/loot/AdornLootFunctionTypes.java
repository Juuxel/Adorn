package juuxel.adorn.loot;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.core.registries.Registries;

public final class AdornLootFunctionTypes {
    public static final Registrar<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES = RegistrarFactory.get().create(Registries.LOOT_FUNCTION_TYPE);
    public static final Registered<LootItemFunctionType<CheckTradingStationOwnerLootFunction>> CHECK_TRADING_STATION_OWNER =
        LOOT_FUNCTION_TYPES.register("check_trading_station_owner", () -> new LootItemFunctionType<>(CheckTradingStationOwnerLootFunction.CODEC));

    public static void init() {
    }
}
