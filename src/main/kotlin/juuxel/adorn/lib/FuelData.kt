package juuxel.adorn.lib

import juuxel.adorn.block.BenchBlock
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.block.TableBlock
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag

data class FuelData(val tag: Tag<Item>, val blockClass: Class<out Block>?, val burnTime: Int) {
    companion object {
        val ALL: Set<FuelData> = setOf(
            FuelData(AdornItemTags.CHAIRS, ChairBlock::class.java, 300),
            FuelData(AdornItemTags.TABLES, TableBlock::class.java, 300),
            FuelData(AdornItemTags.DRAWERS, DrawerBlock::class.java, 300),
            FuelData(AdornItemTags.BENCHES, BenchBlock::class.java, 300),
            FuelData(AdornItemTags.WOODEN_POSTS, null, 300),
            FuelData(AdornItemTags.WOODEN_PLATFORMS, null, 300),
            FuelData(AdornItemTags.WOODEN_STEPS, null, 300),
            FuelData(AdornItemTags.WOODEN_SHELVES, null, 300),
            FuelData(AdornItemTags.SOFAS, SofaBlock::class.java, 150),
        )
    }
}
