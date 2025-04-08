package juuxel.adorn.platform.fabric;

import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.platform.ItemGroupBridge;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

import java.util.List;
import java.util.function.Consumer;

public final class ItemGroupBridgeFabric implements ItemGroupBridge {
    @Override
    public ItemGroup.Builder builder() {
        return FabricItemGroup.builder();
    }

    @Override
    public void addItems(RegistryKey<ItemGroup> group, Consumer<ItemGroupModifyContext> configurator) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> {
            var context = new ItemGroupModifyContext() {
                @Override
                public void add(ItemStack stack) {
                    entries.add(stack);
                }

                @Override
                public void addBefore(ItemConvertible before, List<? extends ItemConvertible> items) {
                    entries.addBefore(before, items.toArray(ItemConvertible[]::new));
                }

                @Override
                public void addAfter(ItemConvertible after, List<? extends ItemConvertible> items) {
                    entries.addAfter(after, items.toArray(ItemConvertible[]::new));
                }
            };

            configurator.accept(context);
        });
    }
}
