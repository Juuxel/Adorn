package juuxel.adorn.platform.fabric

import juuxel.adorn.client.resources.ColorManagerFabric
import juuxel.adorn.platform.ResourceBridge

object ResourceBridgeImpl : ResourceBridge {
    override val colorManager = ColorManagerFabric
}
