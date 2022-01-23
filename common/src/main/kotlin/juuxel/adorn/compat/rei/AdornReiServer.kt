package juuxel.adorn.compat.rei

import juuxel.adorn.AdornCommon
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry
import me.shedaniel.rei.api.common.plugins.REIServerPlugin

open class AdornReiServer : REIServerPlugin {
    override fun registerDisplaySerializer(registry: DisplaySerializerRegistry) {
        registry.register(BREWER, BrewerDisplay.Serializer)
    }

    companion object {
        val BREWER: CategoryIdentifier<BrewerDisplay> = CategoryIdentifier.of(AdornCommon.id("brewer"))
    }
}
