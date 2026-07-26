package juuxel.adorn.platform.fabric;

import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.platform.ItemGroupBridge;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

public final class ItemGroupBridgeFabric implements ItemGroupBridge {
    @Override
    public CreativeModeTab.Builder builder() {
        return FabricCreativeModeTab.builder();
    }

    @Override
    public void addItems(ResourceKey<CreativeModeTab> group, Consumer<ItemGroupModifyContext> configurator) {
        CreativeModeTabEvents.modifyOutputEvent(group).register(entries -> {
            var context = new ItemGroupModifyContext() {
                @Override
                public void add(ItemLike item) {
                    entries.accept(item);
                }

                @Override
                public void add(ItemStack stack) {
                    entries.accept(stack);
                }

                @Override
                public void addBefore(ItemLike before, List<? extends ItemLike> items) {
                    entries.insertBefore(before, items.toArray(ItemLike[]::new));
                }

                @Override
                public void addAfter(ItemLike after, List<? extends ItemLike> items) {
                    entries.insertAfter(after, items.toArray(ItemLike[]::new));
                }

                @Override
                public HolderLookup.Provider getRegistries() {
                    return entries.getContext().holders();
                }
            };

            configurator.accept(context);
        });
    }
}
