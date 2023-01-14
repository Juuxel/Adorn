package juuxel.adorn.block.variant

import juuxel.adorn.util.Displayable
import net.minecraft.text.Text

enum class BlockVariantGroup(id: String) : Displayable {
    WOOD("wood"),
    STONE("stone"),
    WOOL("wool"),
    OTHER("other");

    private val translationKey = "furniture_material_group.adorn.$id"
    override val displayName = Text.translatable(translationKey)
}
