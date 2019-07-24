package juuxel.adorn.item

import juuxel.polyester.block.PolyesterBlock
import juuxel.polyester.item.PolyesterItem
import juuxel.polyester.registry.HasDescription
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.WallStandingBlockItem
import net.minecraft.text.Text
import net.minecraft.world.World

class AdornWallBlockItem(ground: PolyesterBlock, wall: PolyesterBlock, settings: Settings) : WallStandingBlockItem(
    ground.unwrap(),
    wall.unwrap(),
    settings
), PolyesterItem, HasDescription by ground {
    override val name = ground.name

    override fun appendTooltip(p0: ItemStack?, p1: World?, list: MutableList<Text>, p3: TooltipContext?) {
        super.appendTooltip(p0, p1, list, p3)
        PolyesterItem.appendTooltipToList(list, this)
    }
}
