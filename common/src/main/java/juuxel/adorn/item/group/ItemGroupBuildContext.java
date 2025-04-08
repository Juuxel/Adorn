package juuxel.adorn.item.group;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGroupBuildContext {
    void add(ItemStack stack);

    default void add(ItemConvertible item) {
        add(new ItemStack(item));
    }

    default void add(Registered<? extends ItemConvertible> item) {
        add(item.get());
    }
}
