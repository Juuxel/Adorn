package juuxel.adorn.platform

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKey

interface ItemGroupBridge {
    fun builder(): ItemGroup.Builder
    fun addItems(group: RegistryKey<ItemGroup>, configurator: ItemGroupModifyContext.() -> Unit)

    @InlineServices
    companion object {
        private val instance: ItemGroupBridge by lazy { loadService() }
        fun get(): ItemGroupBridge = instance
    }
}
