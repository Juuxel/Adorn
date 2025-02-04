package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryKeys;

public final class AdornSlotDisplays {
    public static final Registrar<SlotDisplay.Serializer<?>> SLOT_DISPLAYS = RegistrarFactory.get().create(RegistryKeys.SLOT_DISPLAY);

    public static void init() {
        SLOT_DISPLAYS.register("fluid_ingredient", () -> FluidIngredientSlotDisplay.SERIALIZER);
    }
}
