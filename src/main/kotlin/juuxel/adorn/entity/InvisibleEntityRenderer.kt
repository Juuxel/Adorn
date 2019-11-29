package juuxel.adorn.entity

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier

class InvisibleEntityRenderer(dispatcher: EntityRenderDispatcher) : EntityRenderer<Entity>(dispatcher) {
    override fun getTexture(entity: Entity) = TEXTURE

    companion object {
        private val TEXTURE = Identifier("missingno")
    }
}
