package juuxel.adorn.item.group;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;

public interface ItemGroupBuildContext {
    void add(ItemConvertible item);
    void add(ItemStack stack);

    RegistryWrapper.WrapperLookup getRegistries();

    default void add(Registered<? extends ItemConvertible> item) {
        add(item.get());
    }
}
