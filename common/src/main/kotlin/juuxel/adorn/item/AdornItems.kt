package juuxel.adorn.item

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.platform.ConfigBridge
import juuxel.adorn.platform.ItemBridge
import juuxel.adorn.platform.Registrar
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object AdornItems {
    val ITEMS: Registrar<Item> = Registrar.item()
    val GROUP = ItemBridge.createAdornItemGroup()

    val STONE_ROD by ITEMS.register("stone_rod") { ItemWithDescription(Item.Settings().group(ItemGroup.MISC)) }

    val STONE_TORCH by ITEMS.register("stone_torch") {
        WallBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    }

    val GUIDE_BOOK by ITEMS.registerOptional("guide_book", ItemBridge::createGuideBook)
    val TRADERS_MANUAL by ITEMS.registerOptional("traders_manual", ItemBridge::createTradersManual)

    fun init() {
    }

    fun isIn(group: ItemGroup?, item: Item): Boolean = when (group) {
        null -> false
        GROUP, ItemGroup.SEARCH -> true
        item.group -> ConfigBridge.get().client.showItemsInStandardGroups
        else -> false
    }
}
