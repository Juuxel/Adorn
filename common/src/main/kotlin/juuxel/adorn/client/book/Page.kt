package juuxel.adorn.client.book

import com.google.gson.JsonElement
import net.minecraft.item.Item
import net.minecraft.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

data class Page(
    val icons: List<Icon>,
    val title: Text,
    val text: Text
) {
    sealed class Icon {
        data class ItemIcon(val item: Item) : Icon()
        data class TagIcon(val tag: TagKey<Item>) : Icon()

        companion object {
            fun fromJson(json: JsonElement): Icon {
                val id = json.asString
                return if (id.startsWith('#')) {
                    TagIcon(TagKey.of(Registry.ITEM_KEY, Identifier(id.substring(1))))
                } else {
                    ItemIcon(Registry.ITEM[Identifier(id)])
                }
            }
        }
    }
}
