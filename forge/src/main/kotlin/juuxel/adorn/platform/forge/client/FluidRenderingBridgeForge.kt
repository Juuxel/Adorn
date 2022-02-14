package juuxel.adorn.platform.forge.client

import juuxel.adorn.menu.FluidVolume
import juuxel.adorn.platform.FluidRenderingBridge
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.fluid.Fluid
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.FluidStack

object FluidRenderingBridgeForge : FluidRenderingBridge {
    private fun stackOf(volume: FluidVolume): FluidStack =
        FluidStack(volume.fluid, volume.amount.toInt(), volume.nbt)

    override val bucketVolume = FluidAttributes.BUCKET_VOLUME.toLong()

    @OnlyIn(Dist.CLIENT)
    override fun getStillSprite(volume: FluidVolume): Sprite? {
        val fluid: Fluid = volume.fluid
        val atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
        return atlas.apply(fluid.attributes.getStillTexture(stackOf(volume)))
    }

    @OnlyIn(Dist.CLIENT)
    override fun getColor(volume: FluidVolume, world: BlockRenderView?, pos: BlockPos?): Int {
        val fluid: Fluid = volume.fluid
        return fluid.attributes.getColor(stackOf(volume))
    }

    @OnlyIn(Dist.CLIENT)
    override fun fillsFromTop(volume: FluidVolume): Boolean {
        val fluid: Fluid = volume.fluid
        return fluid.attributes.isGaseous
    }
}
