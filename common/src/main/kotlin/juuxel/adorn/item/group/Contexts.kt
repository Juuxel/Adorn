package juuxel.adorn.item.group

import net.minecraft.item.ItemConvertible

fun interface ItemGroupBuildContext {
    fun add(item: ItemConvertible)
}

interface ItemGroupModifyContext : ItemGroupBuildContext {
    fun addAfter(after: ItemConvertible, items: List<ItemConvertible>)
}
