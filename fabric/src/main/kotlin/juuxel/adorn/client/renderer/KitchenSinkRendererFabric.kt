package juuxel.adorn.client.renderer

import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric
import juuxel.adorn.fluid.FluidUnit
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.texture.MissingSprite
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import org.slf4j.LoggerFactory

class KitchenSinkRendererFabric(context: BlockEntityRendererFactory.Context) :
    KitchenSinkRenderer<KitchenSinkBlockEntityFabric>(context) {
    override fun getFluidSprite(entity: KitchenSinkBlockEntityFabric): Sprite {
        val sprite = FluidVariantRendering.getSprite(entity.storage.variant)

        if (sprite == null) {
            LOGGER.error("Could not find sprite for fluid variant {} when rendering kitchen sink at {}", entity.storage.variant, entity.pos)
            return MinecraftClient.getInstance()
                .getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(MissingSprite.getMissingSpriteId())
        }

        return sprite
    }

    override fun getFluidColor(entity: KitchenSinkBlockEntityFabric): Int =
        FluidVariantRendering.getColor(entity.storage.variant, entity.world, entity.pos)

    override fun getFluidLevel(entity: KitchenSinkBlockEntityFabric): Double =
        FluidUnit.convertAsDouble(entity.storage.amount.toDouble(), from = FluidUnit.DROPLET, to = FluidUnit.LITRE)

    override fun isEmpty(entity: KitchenSinkBlockEntityFabric): Boolean =
        entity.storage.amount == 0L

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KitchenSinkRendererFabric::class.java)
    }
}
