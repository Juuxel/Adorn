package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService

interface FluidBridge {
    val fluidUnit: FluidUnit

    @InlineServices
    companion object {
        private val instance: FluidBridge by lazy { loadService() }
        @JvmStatic
        fun get(): FluidBridge = instance
    }
}
