package juuxel.adorn.book

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.Item
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
data class Page(
    val icons: List<Item>,
    val title: Text,
    val text: Text
)
