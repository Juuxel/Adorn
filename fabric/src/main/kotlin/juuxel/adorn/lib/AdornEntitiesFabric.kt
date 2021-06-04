package juuxel.adorn.lib

import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import juuxel.adorn.entity.AdornEntities
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry

object AdornEntitiesFabric {
    @Environment(EnvType.CLIENT)
    fun initClient() {
        EntityRendererRegistry.INSTANCE.register(AdornEntities.SEAT, ::InvisibleEntityRenderer)
    }
}
