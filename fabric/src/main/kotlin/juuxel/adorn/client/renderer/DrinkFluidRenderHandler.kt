package juuxel.adorn.client.renderer

import juuxel.adorn.fluid.DrinkFluid
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.client.texture.Sprite
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

class DrinkFluidRenderHandler(private val fluid: DrinkFluid) : FluidRenderHandler {
    override fun getFluidSprites(view: BlockRenderView?, pos: BlockPos?, state: FluidState): Array<Sprite> =
        FluidRenderHandlerRegistry.INSTANCE[Fluids.WATER].getFluidSprites(view, pos, state)

    override fun getFluidColor(view: BlockRenderView?, pos: BlockPos?, state: FluidState): Int = fluid.color
}
