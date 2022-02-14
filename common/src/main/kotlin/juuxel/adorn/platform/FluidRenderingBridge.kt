package juuxel.adorn.platform

import juuxel.adorn.menu.FluidVolume
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.texture.Sprite
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

// TODO: NO NO NO, ENVIRONMENT SHOULD NOT BE HERE!!!
interface FluidRenderingBridge {
    val bucketVolume: Long

    @Environment(EnvType.CLIENT)
    fun getStillSprite(volume: FluidVolume): Sprite?

    @Environment(EnvType.CLIENT)
    fun getColor(volume: FluidVolume, world: BlockRenderView? = null, pos: BlockPos? = null): Int

    @Environment(EnvType.CLIENT)
    fun fillsFromTop(volume: FluidVolume): Boolean

    companion object {
        fun get() = PlatformBridges.get().fluidRendering
    }
}
