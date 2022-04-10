package juuxel.adorn.lib

import juuxel.adorn.item.FuelData
import net.fabricmc.fabric.api.registry.FuelRegistry

object AdornItemsFabric {
    fun init() {
        for (fuelData in FuelData.FUEL_DATA) {
            when (fuelData) {
                is FuelData.ForItem -> FuelRegistry.INSTANCE.add(fuelData.item, fuelData.burnTime)
                is FuelData.ForTag -> FuelRegistry.INSTANCE.add(fuelData.tag, fuelData.burnTime)
            }
        }
    }
}
