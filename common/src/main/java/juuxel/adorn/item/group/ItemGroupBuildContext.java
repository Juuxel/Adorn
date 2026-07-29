package juuxel.adorn.item.group;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface ItemGroupBuildContext {
    void add(ItemLike item);
    void add(ItemStack stack);

    HolderLookup.Provider getRegistries();
}
