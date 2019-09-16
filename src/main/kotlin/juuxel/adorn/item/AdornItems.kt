package juuxel.adorn.item

import juuxel.adorn.Adorn
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object AdornItems : RegistryHelper(Adorn.NAMESPACE) {
    val STONE_ROD = registerItem("stone_rod", ItemWithDescription(Item.Settings().group(ItemGroup.MISC)))

    val STONE_TORCH = registerItem(
        "stone_torch",
        WallBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    )

    fun init() {
        with(FuelRegistry.INSTANCE) {
            add(AdornTags.CHAIRS.item, 300)
            add(AdornTags.DRAWERS.item, 300)
            add(AdornTags.TABLES.item, 300)
            add(AdornTags.WOODEN_POSTS.item, 300)
            add(AdornTags.WOODEN_PLATFORMS.item, 300)
            add(AdornTags.WOODEN_STEPS.item, 300)
            add(AdornTags.WOODEN_SHELVES.item, 300)

            add(AdornTags.SOFAS.item, 150)
        }
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
    }
}
