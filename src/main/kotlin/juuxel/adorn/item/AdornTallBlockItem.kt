package juuxel.adorn.item

import io.github.juuxel.polyester.block.PolyesterBlock
import io.github.juuxel.polyester.item.PolyesterItem
import net.minecraft.item.TallBlockItem

open class AdornTallBlockItem(block: PolyesterBlock, settings: Settings) :
    TallBlockItem(block.unwrap(), settings), PolyesterItem {
    override val name = block.name
}
