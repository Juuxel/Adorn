package juuxel.adorn.loot

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.util.registry.Registry

object AdornLootFunctionTypes {
    val LOOT_FUNCTION_TYPES: Registrar<LootFunctionType> = PlatformBridges.registrarFactory.create(Registry.LOOT_FUNCTION_TYPE_KEY)
    val CHECK_TRADING_STATION_OWNER: LootFunctionType by LOOT_FUNCTION_TYPES.register("check_trading_station_owner") {
        LootFunctionType(CheckTradingStationOwnerLootFunction.Serializer)
    }

    fun init() {
    }
}
