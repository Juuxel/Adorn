package juuxel.adorn.platform.fabric

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.platform.ItemGroupBridge
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey

class ItemGroupBridgeFabric : ItemGroupBridge {
    override fun builder(): ItemGroup.Builder = FabricItemGroup.builder()

    override fun addItems(group: RegistryKey<ItemGroup>, configurator: ItemGroupModifyContext.() -> Unit) {
        ItemGroupEvents.modifyEntriesEvent(group).register { entries ->
            val context = object : ItemGroupModifyContext {
                override fun add(item: ItemConvertible) {
                    entries.add(item)
                }

                override fun addAfter(after: ItemConvertible, items: List<ItemConvertible>) {
                    entries.addAfter(after, *items.toTypedArray())
                }
            }

            configurator(context)
        }
    }
}
