package juuxel.adorn.lib

import juuxel.adorn.Adorn
import juuxel.adorn.item.*
import juuxel.adorn.util.VanillaWoodType
import juuxel.polyester.registry.PolyesterRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val CHAIRS: List<BlockItem> = ModBlocks.CHAIRS.map {
        registerItem(ChairBlockItem(it))
    }

    val TABLES: List<TableBlockItem> = ModBlocks.TABLES.map {
        registerItem(TableBlockItem(it))
    }

    val STONE_ROD = registerItem("stone_rod", ItemWithDescription(Item.Settings().group(ItemGroup.MISC)))

    val STONE_TORCH = registerItem(
        "stone_torch",
        WallBlockItemWithDescription(
            ModBlocks.STONE_TORCH_GROUND,
            ModBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    )

    fun init() {
        with(FuelRegistry.INSTANCE) {
            add(ModTags.CHAIRS.item, 300)
            add(ModTags.DRAWERS.item, 300)
            add(ModTags.TABLES.item, 300)
            add(ModTags.WOODEN_POSTS.item, 300)
            add(ModTags.WOODEN_PLATFORMS.item, 300)
            add(ModTags.WOODEN_STEPS.item, 300)
            add(ModTags.WOODEN_SHELVES.item, 300)

            add(ModTags.SOFAS.item, 150)
        }
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
    }
}
