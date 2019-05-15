package juuxel.adorn.item

import io.github.juuxel.polyester.item.PolyesterBaseItem
import net.minecraft.item.ItemGroup

class StoneRodItem : PolyesterBaseItem("stone_rod", Settings().itemGroup(ItemGroup.MISC)) {
    override val hasDescription = true
}
