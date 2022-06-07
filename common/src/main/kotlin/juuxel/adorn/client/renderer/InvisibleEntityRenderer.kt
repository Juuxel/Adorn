package juuxel.adorn.client.renderer

import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier

class InvisibleEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<Entity>(context) {
    override fun getTexture(entity: Entity) =
        TEXTURE

    companion object {
        private val TEXTURE = Identifier("missingno")
    }
}
