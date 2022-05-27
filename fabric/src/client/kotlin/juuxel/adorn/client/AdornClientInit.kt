package juuxel.adorn.client

import juuxel.adorn.client.gui.screen.AdornMenuScreens
import juuxel.adorn.client.lib.AdornBlocksClient
import juuxel.adorn.client.lib.AdornEntitiesClient
import juuxel.adorn.client.lib.AdornResources
import juuxel.adorn.client.lib.ClientNetworking
import net.fabricmc.api.ClientModInitializer

object AdornClientInit : ClientModInitializer {
    override fun onInitializeClient() {
        AdornBlocksClient.init()
        AdornEntitiesClient.init()
        AdornMenuScreens.register()
        ClientNetworking.init()
        AdornResources.init()
        ClientCompatInit.init()
    }
}
