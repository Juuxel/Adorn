package juuxel.adorn.item

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

open class SimpleAdornItem(settings: Settings) : Item(settings) {
    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)
}
