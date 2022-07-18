package juuxel.adorn.client

import juuxel.adorn.util.ServiceDelegate

interface ClientPlatformBridges {
    val fluidRendering: FluidRenderingBridge

    companion object {
        private val instance: ClientPlatformBridges by ServiceDelegate()
        fun get(): ClientPlatformBridges = instance
    }
}
