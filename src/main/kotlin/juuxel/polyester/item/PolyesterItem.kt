package juuxel.polyester.item

import juuxel.polyester.registry.HasDescription
import juuxel.polyester.registry.PolyesterContent
import net.minecraft.item.Item
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

interface PolyesterItem : PolyesterContent<Item>,
    HasDescription {
    companion object {
        fun appendTooltipToList(list: MutableList<Text>, content: PolyesterItem) = with(content) {
            if (hasDescription) {
                list.add(
                    TranslatableText(
                        descriptionKey.replace(
                            "%TranslationKey",
                            unwrap().translationKey
                        )
                    ).styled {
                        it.isItalic = true
                        it.color = Formatting.DARK_GRAY
                    }
                )
            }
        }
    }
}