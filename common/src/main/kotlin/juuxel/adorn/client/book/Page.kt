package juuxel.adorn.client.book

import net.minecraft.item.Item
import net.minecraft.text.Text

data class Page(
    val icons: List<Item>,
    val title: Text,
    val text: Text
)
