package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.platform.ItemBridge
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor

object ItemBridgeImpl : ItemBridge {
    override fun createAdornItemGroup(): ItemGroup =
        FabricItemGroupBuilder.build(AdornCommon.id("items")) { ItemStack(AdornBlocks.SOFAS[DyeColor.LIME]) }
}
