package juuxel.adorn.platform.forge

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.platform.ItemBridge
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor

object ItemBridgeImpl : ItemBridge {
    override fun createAdornItemGroup(): ItemGroup = object : ItemGroup("adorn.items") {
        override fun createIcon(): ItemStack = ItemStack(AdornBlocks.SOFAS[DyeColor.LIME])
    }
}
