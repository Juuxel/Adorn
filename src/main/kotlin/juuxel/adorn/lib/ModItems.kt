package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.StoneRodItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.util.VanillaWoodType
import net.minecraft.util.registry.Registry

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val CHAIRS: List<ChairBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(ChairBlockItem(ModBlocks.CHAIRS[it]))
    }

    val TABLES: List<TableBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(TableBlockItem(ModBlocks.TABLES[it]))
    }

    val STONE_ROD = register(Registry.ITEM, StoneRodItem())

    fun init() {}
}
