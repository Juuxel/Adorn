package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.ClientPlatformBridges

class ClientPlatformBridgesForge : ClientPlatformBridges {
    override val fluidRendering = FluidRenderingBridgeForge
}
