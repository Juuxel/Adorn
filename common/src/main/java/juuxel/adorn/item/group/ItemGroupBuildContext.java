package juuxel.adorn.item.group;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;

public interface ItemGroupBuildContext {
    void add(ItemLike item);
    void add(ItemStack stack);

    HolderLookup.Provider getRegistries();

    default void add(Registered<? extends ItemLike> item) {
        add(item.get());
    }
}
