package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.util.ServiceDelegate

interface FluidBridge {
    val fluidUnit: FluidUnit

    companion object {
        private val instance: FluidBridge by ServiceDelegate()
        fun get(): FluidBridge = instance
    }
}
