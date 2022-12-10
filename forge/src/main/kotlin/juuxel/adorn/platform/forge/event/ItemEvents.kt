package juuxel.adorn.platform.forge.event

import juuxel.adorn.item.FuelData
import juuxel.adorn.platform.ItemGroupBridge
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent

object ItemEvents {
    fun register(modBus: IEventBus, forgeBus: IEventBus) {
        forgeBus.register(this)
        modBus.register(ItemGroupBridge.get())
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
