package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidReference
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.texture.Sprite
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

// TODO: NO NO NO, ENVIRONMENT SHOULD NOT BE HERE!!!
interface FluidRenderingBridge {
    val bucketVolume: Long

    @Environment(EnvType.CLIENT)
    fun getStillSprite(volume: FluidReference): Sprite?

    @Environment(EnvType.CLIENT)
    fun getColor(volume: FluidReference, world: BlockRenderView? = null, pos: BlockPos? = null): Int

    @Environment(EnvType.CLIENT)
    fun fillsFromTop(volume: FluidReference): Boolean

    @Environment(EnvType.CLIENT)
    fun getTooltip(volume: FluidReference, context: TooltipContext, maxAmountInLitres: Int?): List<Text>

    companion object {
        fun get() = PlatformBridges.get().fluidRendering
    }
}
