package juuxel.adorn.item

import io.github.cottonmc.cotton.util.Identifiers
import io.github.juuxel.polyester.item.PolyesterBaseItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.SystemUtil
import net.minecraft.util.registry.Registry

class StoneRodItem : PolyesterBaseItem("stone_rod", Settings().group(ItemGroup.MISC)) {
    override val hasDescription = true
    private val _translationKey: String by lazy {
        SystemUtil.createTranslationKey("item", Identifiers.withNamespace(Registry.ITEM.getId(this), "adorn"))
    }

    override fun getOrCreateTranslationKey() = _translationKey
}
