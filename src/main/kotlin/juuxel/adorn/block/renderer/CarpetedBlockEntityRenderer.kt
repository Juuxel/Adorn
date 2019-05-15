package juuxel.adorn.block.renderer

import com.mojang.blaze3d.platform.GlStateManager
import juuxel.adorn.block.entity.CarpetedBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.texture.SpriteAtlasTexture

class CarpetedBlockEntityRenderer : BlockEntityRenderer<CarpetedBlockEntity>() {
    private val manager = MinecraftClient.getInstance().blockRenderManager

    override fun render(be: CarpetedBlockEntity, x: Double, y: Double, z: Double, tickDelta: Float, i: Int) {
        super.render(be, x, y, z, tickDelta, i)
        if (be.carpet != null) {
            val tessellator = Tessellator.getInstance()
            val bufferBuilder = tessellator.bufferBuilder
            val carpetState = be.carpetState

            bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
            bufferBuilder.setOffset(x, y, z)

            manager.modelRenderer.tesselate(
                world,
                manager.getModel(carpetState),
                carpetState,
                be.pos,
                bufferBuilder,
                false,
                world.random,
                carpetState.getRenderingSeed(be.pos)
            )

            tessellator.draw()
        }
    }
}
