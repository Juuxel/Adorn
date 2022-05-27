package juuxel.adorn.client

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.util.Fractions
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object FluidRenderingBridgeFabric : FluidRenderingBridge {
    private fun variantOf(volume: FluidReference): FluidVariant =
        FluidVariant.of(volume.fluid, volume.nbt)

    @Environment(EnvType.CLIENT)
    override fun getStillSprite(volume: FluidReference): Sprite? =
        FluidVariantRendering.getSprite(variantOf(volume))

    @Environment(EnvType.CLIENT)
    override fun getColor(volume: FluidReference, world: BlockRenderView?, pos: BlockPos?) =
        FluidVariantRendering.getColor(variantOf(volume), world, pos)

    @Environment(EnvType.CLIENT)
    override fun fillsFromTop(volume: FluidReference): Boolean =
        FluidVariantAttributes.isLighterThanAir(variantOf(volume))

    @Environment(EnvType.CLIENT)
    override fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text> {
        val result = FluidVariantRendering.getTooltip(variantOf(volume), context).toMutableList()

        if (maxAmountInLitres != null) {
            result.add(
                1,
                TranslatableText(
                    "gui.adorn.litres_fraction",
                    Fractions.toString(volume.amount, FluidConstants.BUCKET / 1000),
                    maxAmountInLitres
                )
            )
        } else {
            result.add(
                1,
                TranslatableText(
                    "gui.adorn.litres",
                    Fractions.toString(volume.amount, FluidConstants.BUCKET / 1000)
                )
            )
        }

        return result
    }
}
