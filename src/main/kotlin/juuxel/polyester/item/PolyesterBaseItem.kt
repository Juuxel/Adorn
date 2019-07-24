package juuxel.polyester.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

open class PolyesterBaseItem(override val name: String, settings: Settings) : Item(settings),
    PolyesterItem {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        list: MutableList<Text>,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, list, context)
        PolyesterItem.appendTooltipToList(list, this)
    }
}
