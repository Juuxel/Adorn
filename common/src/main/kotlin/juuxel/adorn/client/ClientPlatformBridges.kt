package juuxel.adorn.client

import java.util.ServiceLoader

interface ClientPlatformBridges {
    val fluidRendering: FluidRenderingBridge

    companion object {
        private val instance: ClientPlatformBridges by lazy {
            val serviceLoader = ServiceLoader.load(ClientPlatformBridges::class.java)
            serviceLoader.findFirst().orElseThrow { RuntimeException("Could not find Adorn client platform instance") }
        }

        fun get(): ClientPlatformBridges = instance
    }
}
