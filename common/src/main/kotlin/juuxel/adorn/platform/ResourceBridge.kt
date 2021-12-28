package juuxel.adorn.platform

import juuxel.adorn.client.book.BookManager
import juuxel.adorn.client.resources.ColorManager

interface ResourceBridge {
    val bookManager: BookManager
    val colorManager: ColorManager
}
