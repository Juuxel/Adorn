package juuxel.adorn.client

import juuxel.adorn.client.renderer.DrinkFluidRenderHandler
import juuxel.adorn.fluid.AdornFluids
import juuxel.adorn.fluid.DrinkFluid
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry

object FluidRendering {
    fun init() {
        registerDrinkRenderHandler(AdornFluids.SWEET_BERRY_JUICE)
    }

    private fun registerDrinkRenderHandler(still: DrinkFluid) {
        FluidRenderHandlerRegistry.INSTANCE.register(still, still.flowing, DrinkFluidRenderHandler(still))
    }
}
