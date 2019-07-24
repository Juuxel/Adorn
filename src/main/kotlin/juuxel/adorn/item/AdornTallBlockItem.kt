package juuxel.adorn.item

import juuxel.polyester.block.PolyesterBlock
import juuxel.polyester.item.PolyesterItem
import net.minecraft.item.TallBlockItem

open class AdornTallBlockItem(block: PolyesterBlock, settings: Settings) :
    TallBlockItem(block.unwrap(), settings), PolyesterItem {
    override val name = block.name
}
