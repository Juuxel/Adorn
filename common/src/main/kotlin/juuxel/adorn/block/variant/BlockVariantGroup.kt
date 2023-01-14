package juuxel.adorn.block.variant

import juuxel.adorn.util.Displayable
import net.minecraft.text.Text

enum class BlockVariantGroup(id: String) : Displayable {
    WOOD("wood"),
    STONE("stone"),
    METAL("metal"),
    GLASS("glass"),
    WOOL("wool"),
    FUNCTIONAL("functional"),
    OTHER("other");

    private val translationKey = "furniture_material_group.adorn.$id"
    override val displayName = Text.translatable(translationKey)
}
