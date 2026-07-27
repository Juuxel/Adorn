package juuxel.adorn.event;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.item.FuelData;
import juuxel.adorn.platform.ItemGroupBridge;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.tooltip.TooltipAppender;
import net.neoforged.neoforge.event.RegisterTooltipAppendersEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public final class ItemEvents {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(ItemEvents::onFuelTime);
        modBus.register(ItemGroupBridge.get());
        modBus.addListener(ItemEvents::registerTooltipAppenders);
    }

    private static void registerTooltipAppenders(RegisterTooltipAppendersEvent event) {
        for (var componentProvider : AdornComponentTypes.getTooltipComponents()) {
            DataComponentType<? extends TooltipProvider> component = componentProvider.get();
            event.registerComponentAppenderBeforeAll(component, TooltipAppender.createComponentAppender(component));
        }
    }

    private static void onFuelTime(FurnaceFuelBurnTimeEvent event) {
        for (var fuelData : FuelData.FUEL_DATA) {
            if (fuelData.matches(event.getItemStack())) {
                event.setBurnTime(fuelData.burnTime());
                break;
            }
        }
    }
}
