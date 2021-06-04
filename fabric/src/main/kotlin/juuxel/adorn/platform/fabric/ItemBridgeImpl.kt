package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.item.AdornBookItem
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Rarity

object ItemBridgeImpl {
    @JvmStatic
    fun createAdornItemGroup(): ItemGroup =
        FabricItemGroupBuilder.build(AdornCommon.id("items")) { ItemStack(AdornBlocks.SOFAS[DyeColor.LIME]) }

    @JvmStatic
    fun createGuideBook(): Item =
        AdornBookItem(AdornCommon.id("guide"), Item.Settings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON))

    @JvmStatic
    fun createTradersManual(): Item =
        AdornBookItem(AdornCommon.id("traders_manual"), Item.Settings().group(ItemGroup.MISC))
}
