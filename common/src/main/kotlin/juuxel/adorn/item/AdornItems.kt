package juuxel.adorn.item

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Rarity

object AdornItems {
    @JvmField
    val ITEMS: Registrar<Item> = PlatformBridges.registrarFactory.item()
    val GROUP = PlatformBridges.items.createAdornItemGroup()

    val STONE_ROD by ITEMS.register("stone_rod") { ItemWithDescription(Item.Settings().group(ItemGroup.MISC)) }

    val STONE_TORCH by ITEMS.register("stone_torch") {
        WallBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    }

    val GUIDE_BOOK by ITEMS.register("guide_book") {
        AdornBookItem(AdornCommon.id("guide"), Item.Settings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON))
    }

    val TRADERS_MANUAL by ITEMS.register("traders_manual") {
        AdornBookItem(AdornCommon.id("traders_manual"), Item.Settings().group(ItemGroup.MISC))
    }

    fun init() {
    }

    fun isIn(group: ItemGroup?, item: Item): Boolean = when (group) {
        null -> false
        GROUP, ItemGroup.SEARCH -> true
        item.group -> PlatformBridges.configManager.config.client.showItemsInStandardGroups
        else -> false
    }
}
