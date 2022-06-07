package juuxel.adorn.client.resources

import juuxel.adorn.AdornCommon
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener

object ColorManagerFabric : ColorManager(), IdentifiableResourceReloadListener {
    private val ID = AdornCommon.id("color_manager")

    override fun getFabricId() = ID
}
