package juuxel.adorn.lib

import io.github.cottonmc.cotton.registry.CommonItems
import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.AdornWallBlockItem
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.StoneRodItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.util.VanillaWoodType
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val CHAIRS: List<ChairBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(ChairBlockItem(ModBlocks.CHAIRS[it]))
    }

    val TABLES: List<TableBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(TableBlockItem(ModBlocks.TABLES[it]))
    }

    val STONE_ROD = CommonItems.register("stone_rod", StoneRodItem())

    val STONE_TORCH = registerItem(
        AdornWallBlockItem(
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

            add(ModTags.SOFAS.item, 150)
        }
    }
}
