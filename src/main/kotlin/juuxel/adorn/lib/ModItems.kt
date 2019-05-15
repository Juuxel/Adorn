package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.AdornWallBlockItem
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.StoneRodItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.util.VanillaWoodType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val CHAIRS: List<ChairBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(ChairBlockItem(ModBlocks.CHAIRS[it]))
    }

    val TABLES: List<TableBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(TableBlockItem(ModBlocks.TABLES[it]))
    }

    val STONE_ROD = register(Registry.ITEM, StoneRodItem())

    val STONE_TORCH = register(
        Registry.ITEM,
        AdornWallBlockItem(
            ModBlocks.STONE_TORCH_GROUND,
            ModBlocks.STONE_TORCH_WALL,
            Item.Settings().itemGroup(ItemGroup.DECORATIONS)
        )
    )

    fun init() {}
}
