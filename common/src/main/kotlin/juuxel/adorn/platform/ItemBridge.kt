package juuxel.adorn.platform

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

interface ItemBridge {
    fun createAdornItemGroup(): ItemGroup

    // Platform-specific items
    fun createGuideBook(): Item?
    fun createTradersManual(): Item?
}
