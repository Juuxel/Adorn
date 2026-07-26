package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.core.registries.Registries;

public final class AdornSlotDisplays {
    public static final Registrar<SlotDisplay.Type<?>> SLOT_DISPLAYS = RegistrarFactory.get().create(Registries.SLOT_DISPLAY);

    public static void init() {
        SLOT_DISPLAYS.register("fluid_ingredient", () -> FluidIngredientSlotDisplay.SERIALIZER);
    }
}
