package juuxel.adorn.loot

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.registry.RegistryKeys

object AdornLootFunctionTypes {
    val LOOT_FUNCTION_TYPES: Registrar<LootFunctionType> = RegistrarFactory.get().create(RegistryKeys.LOOT_FUNCTION_TYPE)
    val CHECK_TRADING_STATION_OWNER: LootFunctionType by LOOT_FUNCTION_TYPES.register("check_trading_station_owner") {
        LootFunctionType(CheckTradingStationOwnerLootFunction.CODEC)
    }

    fun init() {
    }
}
