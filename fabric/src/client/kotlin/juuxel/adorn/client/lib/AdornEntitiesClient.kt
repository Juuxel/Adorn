package juuxel.adorn.client.lib

import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import juuxel.adorn.entity.AdornEntities
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object AdornEntitiesClient {
    fun init() {
        EntityRendererRegistry.register(AdornEntities.SEAT, ::InvisibleEntityRenderer)
    }
}
