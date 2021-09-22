package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import juuxel.adorn.entity.AdornEntities
import net.minecraftforge.client.event.EntityRenderersEvent

object AdornRenderers {
    fun registerRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(AdornEntities.SEAT, ::InvisibleEntityRenderer)
    }
}
