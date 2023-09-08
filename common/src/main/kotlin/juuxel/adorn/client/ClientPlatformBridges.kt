package juuxel.adorn.client

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService

interface ClientPlatformBridges {
    val fluidRendering: FluidRenderingBridge

    @InlineServices
    companion object {
        private val instance: ClientPlatformBridges by lazy { loadService() }
        fun get(): ClientPlatformBridges = instance
    }
}
