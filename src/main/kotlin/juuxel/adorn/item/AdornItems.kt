package juuxel.adorn.item

import juuxel.adorn.Adorn
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.config.Config
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.WallStandingBlockItem
import net.minecraft.util.DyeColor
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object AdornItems {
    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Adorn.NAMESPACE)

    val GROUP = object : ItemGroup(Adorn.id("items").toString()) {
        override fun createIcon() = ItemStack(AdornBlocks.SOFAS.getValue(DyeColor.LIME).get())
    }

    val STONE_ROD = ITEMS.register("stone_rod") { ItemWithDescription(Item.Settings().group(ItemGroup.MISC)) }

    val STONE_TORCH = ITEMS.register("stone_torch") {
        WallStandingBlockItem(
            AdornBlocks.STONE_TORCH_GROUND.get(),
            AdornBlocks.STONE_TORCH_WALL.get(),
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    }

    fun isIn(group: ItemGroup?, item: Item): Boolean = when (group) {
        null -> false
        GROUP, ItemGroup.SEARCH -> true
        item.group -> Config.SHOW_ITEMS_IN_STANDARD_GROUPS.get()
        else -> false
    }
}
