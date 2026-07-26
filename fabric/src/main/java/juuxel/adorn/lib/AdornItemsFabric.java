package juuxel.adorn.lib;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.item.FuelData;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;

public final class AdornItemsFabric {
    public static void init() {
        FuelValueEvents.BUILD.register((builder, _) -> {
            for (var fuelData : FuelData.FUEL_DATA) {
                switch (fuelData) {
                    case FuelData.ForItem(var item, int burnTime) -> builder.add(item.get(), burnTime);
                    case FuelData.ForTag(var tag, int burnTime) -> builder.add(tag, burnTime);
                }
            }
        });

        for (var component : AdornComponentTypes.getTooltipComponents()) {
            ItemComponentTooltipProviderRegistry.addFirst(component.get());
        }
    }
}
