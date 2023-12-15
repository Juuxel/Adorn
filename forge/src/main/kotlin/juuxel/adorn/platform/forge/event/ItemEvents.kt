package juuxel.adorn.platform.forge.event

import juuxel.adorn.item.FuelData
import juuxel.adorn.platform.ItemGroupBridge
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent

object ItemEvents {
    fun register(modBus: IEventBus) {
        NeoForge.EVENT_BUS.register(this)
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
