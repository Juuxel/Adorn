package juuxel.adorn.platform.forge

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.platform.ItemGroupBridge
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKey
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ItemGroupBridgeForge : ItemGroupBridge {
    private val additions: MutableList<Pair<RegistryKey<ItemGroup>, ItemGroupModifyContext.() -> Unit>> = ArrayList()

    override fun builder(): ItemGroup.Builder = ItemGroup.builder()

    override fun addItems(group: RegistryKey<ItemGroup>, configurator: ItemGroupModifyContext.() -> Unit) {
        additions += group to configurator
    }

    @SubscribeEvent
    fun addToGroups(event: BuildCreativeModeTabContentsEvent) {
        for ((group, configurator) in additions) {
            val context = object : ItemGroupModifyContext {
                override fun add(item: ItemConvertible) {
                    if (event.tabKey == group) {
                        event.add(item)
                    }
                }

                override fun addAfter(after: ItemConvertible, items: List<ItemConvertible>) {
                    if (event.tabKey == group) {
                        items.fold(ItemStack(after)) { nextAfter, item ->
                            val stack = ItemStack(item)
                            event.entries.putAfter(nextAfter, stack, DEFAULT_STACK_VISIBILITY)
                            stack
                        }
                    }
                }
            }
            configurator(context)
        }
    }

    companion object {
        private val DEFAULT_STACK_VISIBILITY = ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
    }
}
