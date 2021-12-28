package juuxel.adorn.platform.forge

import juuxel.adorn.client.book.BookManager
import juuxel.adorn.client.resources.ColorManager
import juuxel.adorn.platform.ResourceBridge

object ResourceBridgeImpl : ResourceBridge {
    override val bookManager = BookManager()
    override val colorManager = ColorManager()
}
