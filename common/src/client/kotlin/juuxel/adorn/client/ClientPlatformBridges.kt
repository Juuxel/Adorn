package juuxel.adorn.client

import java.util.ServiceLoader

interface ClientPlatformBridges {
    val fluidRendering: juuxel.adorn.client.FluidRenderingBridge

    companion object {
        private val instance: juuxel.adorn.client.ClientPlatformBridges by lazy {
            val serviceLoader = ServiceLoader.load(juuxel.adorn.client.ClientPlatformBridges::class.java)
            serviceLoader.findFirst().orElseThrow { RuntimeException("Could not find Adorn client platform instance") }
        }

        fun get(): juuxel.adorn.client.ClientPlatformBridges = juuxel.adorn.client.ClientPlatformBridges.Companion.instance
    }
}
