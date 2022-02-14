package juuxel.adorn.platform.fabric

import juuxel.adorn.menu.FluidVolume
import juuxel.adorn.platform.FluidRenderingBridge
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.texture.Sprite
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object FluidRenderingBridgeFabric : FluidRenderingBridge {
    private fun variantOf(volume: FluidVolume): FluidVariant =
        FluidVariant.of(volume.fluid, volume.nbt)

    override val bucketVolume = FluidConstants.BUCKET

    @Environment(EnvType.CLIENT)
    override fun getStillSprite(volume: FluidVolume): Sprite? =
        FluidVariantRendering.getSprite(variantOf(volume))

    @Environment(EnvType.CLIENT)
    override fun getColor(volume: FluidVolume, world: BlockRenderView?, pos: BlockPos?) =
        FluidVariantRendering.getColor(variantOf(volume), world, pos)

    @Environment(EnvType.CLIENT)
    override fun fillsFromTop(volume: FluidVolume): Boolean =
        FluidVariantRendering.fillsFromTop(variantOf(volume))
}
