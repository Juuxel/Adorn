package juuxel.adorn.platform.forge;

import com.mojang.datafixers.util.Pair;
import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.platform.ItemGroupBridge;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemGroupBridgeForge implements ItemGroupBridge {
    private static final CreativeModeTab.TabVisibility DEFAULT_STACK_VISIBILITY = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    private final List<Pair<ResourceKey<CreativeModeTab>, Consumer<ItemGroupModifyContext>>> additions = new ArrayList<>();

    @Override
    public CreativeModeTab.Builder builder() {
        return CreativeModeTab.builder();
    }

    @Override
    public void addItems(ResourceKey<CreativeModeTab> group, Consumer<ItemGroupModifyContext> configurator) {
        additions.add(new Pair<>(group, configurator));
    }

    @SubscribeEvent
    public void addToGroups(BuildCreativeModeTabContentsEvent event) {
        for (var entry : additions) {
            var group = entry.getFirst();
            var configurator = entry.getSecond();
            var context = new ItemGroupModifyContext() {
                @Override
                public void add(ItemLike item) {
                    if (event.getTabKey().equals(group)) {
                        event.accept(item);
                    }
                }

                @Override
                public void add(ItemStack stack) {
                    if (event.getTabKey().equals(group)) {
                        event.accept(stack);
                    }
                }

                @Override
                public void addBefore(ItemLike before, List<? extends ItemLike> items) {
                    if (event.getTabKey().equals(group)) {
                        var allEntries = event.getParentEntries();
                        var beforeStack = new ItemStack(before);
                        for (ItemLike item : items.reversed()) {
                            var stack = new ItemStack(item);
                            if (allEntries.contains(beforeStack)) {
                                event.insertBefore(beforeStack, stack, DEFAULT_STACK_VISIBILITY);
                            } else {
                                event.accept(stack);
                            }
                            beforeStack = stack;
                        }
                    }
                }

                @Override
                public void addAfter(ItemLike after, List<? extends ItemLike> items) {
                    if (event.getTabKey().equals(group)) {
                        var allEntries = event.getParentEntries();
                        var afterStack = new ItemStack(after);
                        for (ItemLike item : items) {
                            var stack = new ItemStack(item);
                            if (allEntries.contains(afterStack)) {
                                event.insertAfter(afterStack, stack, DEFAULT_STACK_VISIBILITY);
                            } else {
                                event.accept(stack);
                            }
                            afterStack = stack;
                        }
                    }
                }

                @Override
                public HolderLookup.Provider getRegistries() {
                    return event.getParameters().holders();
                }
            };
            configurator.accept(context);
        }
    }
}
