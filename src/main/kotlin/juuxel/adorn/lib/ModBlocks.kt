package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.block.TableBlock
import juuxel.adorn.util.WoodType
import net.minecraft.util.DyeColor

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(it.getName()))
    }

    val CHAIRS: List<ChairBlock> = WoodType.values().map {
        registerBlock(ChairBlock(it.id))
    }

    val TABLES: List<TableBlock> = WoodType.values().map {
        registerBlock(TableBlock(it.id))
    }

    fun init() {}
}
