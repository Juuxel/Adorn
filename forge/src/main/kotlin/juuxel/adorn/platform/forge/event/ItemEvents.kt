package juuxel.adorn.platform.forge.event

import juuxel.adorn.item.FuelData
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent

object ItemEvents {
    fun register(forgeBus: IEventBus) {
        forgeBus.register(this)
    }

    @SubscribeEvent
    fun onFuelTime(event: FurnaceFuelBurnTimeEvent) {
        for (fuelData in FuelData.FUEL_DATA) {
            if (fuelData.matches(event.itemStack)) {
                event.burnTime = fuelData.burnTime
                break
            }
        }
    }
}
