package juuxel.adorn.platform

import juuxel.adorn.item.group.ItemGroupModifyContext
import juuxel.adorn.lib.Registered
import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier

interface ItemGroupBridge {
    fun register(id: Identifier, configurator: ItemGroup.Builder.() -> Unit): Registered<ItemGroup>
    fun addItems(group: ItemGroup, configurator: ItemGroupModifyContext.() -> Unit)

    @InlineServices
    companion object {
        private val instance: ItemGroupBridge by lazy { loadService() }
        fun get(): ItemGroupBridge = instance
    }
}
