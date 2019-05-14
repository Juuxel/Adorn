package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.util.VanillaWoodType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    // TODO: Update WoodFurnitureBuilder for this
    val CHAIRS: List<ChairBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(ChairBlockItem(ModBlocks.CHAIRS[it], Item.Settings().itemGroup(ItemGroup.DECORATIONS)))
    }

    val TABLES: List<TableBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(TableBlockItem(ModBlocks.TABLES[it], Item.Settings().itemGroup(ItemGroup.DECORATIONS)))
    }

    fun init() {}
}
