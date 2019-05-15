package juuxel.adorn.item

import io.github.juuxel.polyester.registry.HasDescription
import io.github.juuxel.polyester.registry.PolyesterBlock
import io.github.juuxel.polyester.registry.PolyesterItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.WallStandingBlockItem
import net.minecraft.network.chat.Component
import net.minecraft.world.World

class AdornWallBlockItem(ground: PolyesterBlock, wall: PolyesterBlock, settings: Settings) : WallStandingBlockItem(
    ground.unwrap(),
    wall.unwrap(),
    settings
), PolyesterItem, HasDescription by ground {
    override val name = ground.name

    override fun buildTooltip(p0: ItemStack?, p1: World?, list: MutableList<Component>, p3: TooltipContext?) {
        super.buildTooltip(p0, p1, list, p3)
        PolyesterItem.appendTooltipToList(list, this)
    }
}
