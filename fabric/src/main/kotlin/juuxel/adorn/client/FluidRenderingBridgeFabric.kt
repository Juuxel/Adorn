package juuxel.adorn.client

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.util.toFluidVariant
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object FluidRenderingBridgeFabric : FluidRenderingBridge {
    @Environment(EnvType.CLIENT)
    override fun getStillSprite(volume: FluidReference): Sprite? =
        FluidVariantRendering.getSprite(volume.toFluidVariant())

    @Environment(EnvType.CLIENT)
    override fun getColor(volume: FluidReference, world: BlockRenderView?, pos: BlockPos?) =
        FluidVariantRendering.getColor(volume.toFluidVariant(), world, pos)

    @Environment(EnvType.CLIENT)
    override fun fillsFromTop(volume: FluidReference): Boolean =
        FluidVariantAttributes.isLighterThanAir(volume.toFluidVariant())

    @Environment(EnvType.CLIENT)
    override fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text> {
        val result = FluidVariantRendering.getTooltip(volume.toFluidVariant(), context).toMutableList()

        if (maxAmountInLitres != null) {
            result.add(1, volume.getAmountText(maxAmountInLitres.toLong(), FluidUnit.LITRE))
        } else {
            result.add(1, volume.getAmountText())
        }

        return result
    }
}
