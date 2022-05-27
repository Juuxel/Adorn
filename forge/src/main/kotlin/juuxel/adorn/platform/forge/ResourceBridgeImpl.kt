package juuxel.adorn.platform.forge

import juuxel.adorn.item.book.BookManager
import juuxel.adorn.platform.ResourceBridge
import juuxel.adorn.resources.ColorManager

object ResourceBridgeImpl : ResourceBridge {
    override val bookManager = BookManager()
    override val colorManager = ColorManager()
}
