package juuxel.adorn.item

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.lib.AdornTags
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.TagKey

sealed interface FuelData {
    val burnTime: Int
    fun matches(stack: ItemStack): Boolean

    data class ForItem(val item: ItemConvertible, override val burnTime: Int) : FuelData {
        override fun matches(stack: ItemStack): Boolean {
            return stack.isOf(item.asItem())
        }
    }

    data class ForTag(val tag: TagKey<Item>, override val burnTime: Int) : FuelData {
        override fun matches(stack: ItemStack): Boolean {
            return stack.isIn(tag)
        }
    }

    companion object {
        @JvmField
        val FUEL_DATA = setOf(
            // Wooden (300)
            ForTag(AdornTags.CHAIRS.item, 300),
            ForTag(AdornTags.DRAWERS.item, 300),
            ForTag(AdornTags.TABLES.item, 300),
            ForTag(AdornTags.BENCHES.item, 300),
            ForTag(AdornTags.WOODEN_POSTS.item, 300),
            ForTag(AdornTags.WOODEN_PLATFORMS.item, 300),
            ForTag(AdornTags.WOODEN_STEPS.item, 300),
            ForTag(AdornTags.WOODEN_SHELVES.item, 300),
            ForItem(AdornBlocks.CRATE, 300),
            // Woollen (150)
            ForTag(AdornTags.SOFAS.item, 150)
        )
    }
}
