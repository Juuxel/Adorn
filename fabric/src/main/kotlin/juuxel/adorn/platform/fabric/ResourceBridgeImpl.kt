package juuxel.adorn.platform.fabric

import juuxel.adorn.client.resources.BookManagerFabric
import juuxel.adorn.client.resources.ColorManagerFabric
import juuxel.adorn.platform.ResourceBridge

object ResourceBridgeImpl : ResourceBridge {
    override val bookManager = BookManagerFabric
    override val colorManager = ColorManagerFabric
}
