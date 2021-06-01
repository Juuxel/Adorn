package juuxel.adorn.lib

import net.fabricmc.fabric.api.registry.FuelRegistry

object AdornItemsFabric {
    fun init() {
        with(FuelRegistry.INSTANCE) {
            add(AdornTags.CHAIRS.item, 300)
            add(AdornTags.DRAWERS.item, 300)
            add(AdornTags.TABLES.item, 300)
            add(AdornTags.WOODEN_POSTS.item, 300)
            add(AdornTags.WOODEN_PLATFORMS.item, 300)
            add(AdornTags.WOODEN_STEPS.item, 300)
            add(AdornTags.WOODEN_SHELVES.item, 300)

            add(AdornTags.SOFAS.item, 150)
        }
    }
}
