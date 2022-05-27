package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.ResourceBridge
import juuxel.adorn.resources.BookManagerFabric
import juuxel.adorn.resources.ColorManagerFabric

object ResourceBridgeImpl : ResourceBridge {
    override val bookManager = BookManagerFabric
    override val colorManager = ColorManagerFabric
}
