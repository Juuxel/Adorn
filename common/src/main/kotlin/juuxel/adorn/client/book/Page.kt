package juuxel.adorn.client.book

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.MoreCodecs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.Optional

data class Page(
    val icons: List<Icon>,
    val title: Text,
    val text: Text,
    val image: Image?
) {
    // For DFU
    private constructor(icons: List<Icon>, title: Text, text: Text, image: Optional<Image>) :
        this(icons, title, text, image.orElse(null))

    companion object {
        val CODEC: Codec<Page> = RecordCodecBuilder.create { instance ->
            instance.group(
                Icon.CODEC.listOf().fieldOf("icons").forGetter { it.icons },
                MoreCodecs.TEXT.fieldOf("title").forGetter { it.title },
                MoreCodecs.TEXT.optionalFieldOf("text", Text.empty()).forGetter { it.text },
                Image.CODEC.optionalFieldOf("image").forGetter { Optional.ofNullable(it.image) }
            ).apply(instance, ::Page)
        }
    }

    sealed class Icon {
        abstract fun createStacks(): List<ItemStack>

        data class ItemIcon(val item: Item) : Icon() {
            override fun createStacks(): List<ItemStack> =
                listOf(item.defaultStack)
        }

        data class TagIcon(val tag: TagKey<Item>) : Icon() {
            override fun createStacks(): List<ItemStack> =
                Registries.ITEM.getOrCreateEntryList(tag).map { it.value().defaultStack }
        }

        companion object {
            val CODEC: Codec<Icon> = object : Codec<Icon> {
                override fun <T> encode(input: Icon, ops: DynamicOps<T>, prefix: T): DataResult<T> {
                    val id = when (input) {
                        is ItemIcon -> Registries.ITEM.getId(input.item).toString()
                        is TagIcon -> "#${input.tag.id}"
                    }
                    return ops.mergeToPrimitive(prefix, ops.createString(id))
                }

                override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Icon, T>> =
                    ops.getStringValue(input).map { id ->
                        if (id.startsWith('#')) {
                            TagIcon(TagKey.of(RegistryKeys.ITEM, Identifier(id.substring(1))))
                        } else {
                            ItemIcon(Registries.ITEM[Identifier(id)])
                        }
                    }.map { Pair.of(it, ops.empty()) }
            }
        }
    }
}
