package juuxel.adorn.item.group

import juuxel.adorn.util.Displayable
import net.minecraft.text.Text

enum class ItemGroupingOption(val id: String) : Displayable {
    BY_MATERIAL("by_material"),
    BY_SHAPE("by_shape");

    override val displayName: Text = Text.translatable("gui.adorn.item_grouping_option.$id")
}
