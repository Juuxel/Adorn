package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidUnit

interface FluidBridge {
    val fluidUnit: FluidUnit

    companion object {
        fun get(): FluidBridge = PlatformBridges.get().fluids
    }
}
