package juuxel.adorn.lib

import juuxel.adorn.item.FuelData
import net.fabricmc.fabric.api.registry.FuelRegistry

object AdornItemsFabric {
    fun init() {
        for (fuelData in FuelData.FUEL_DATA) {
            FuelRegistry.INSTANCE.add(fuelData.tag, fuelData.burnTime)
        }
    }
}
