package juuxel.adorn.platform

import juuxel.adorn.item.book.BookManager
import juuxel.adorn.resources.ColorManager

interface ResourceBridge {
    val bookManager: BookManager
    val colorManager: ColorManager
}
