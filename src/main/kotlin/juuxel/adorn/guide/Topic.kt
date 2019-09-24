package juuxel.adorn.guide

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.Item
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
data class Topic(
    val icons: List<Item>,
    val title: Text,
    val text: Text
)
