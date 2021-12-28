package juuxel.adorn.platform

import net.minecraft.item.ItemGroup

interface ItemBridge {
    fun createAdornItemGroup(): ItemGroup
}
