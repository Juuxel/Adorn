package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public final class AdornLootFunctionTypes {
    public static final Registrar<MapCodec<? extends LootItemFunction>> LOOT_FUNCTION_TYPES = RegistrarFactory.get().create(Registries.LOOT_FUNCTION_TYPE);
    public static final Registered<MapCodec<CheckTradingStationOwnerLootFunction>> CHECK_TRADING_STATION_OWNER =
        LOOT_FUNCTION_TYPES.register("check_trading_station_owner", () -> CheckTradingStationOwnerLootFunction.CODEC);

    public static void init() {
    }
}
