package juuxel.adorn.client.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.MoreCodecs
import net.minecraft.text.Text

data class Book(
    val title: Text,
    val subtitle: Text,
    val author: Text,
    val pages: List<Page>,
    val titleScale: Float
) {
    companion object {
        val CODEC: Codec<Book> = RecordCodecBuilder.create { instance ->
            instance.group(
                MoreCodecs.TEXT.fieldOf("title").forGetter { it.title },
                MoreCodecs.TEXT.fieldOf("subtitle").forGetter { it.subtitle },
                MoreCodecs.TEXT.fieldOf("author").forGetter { it.author },
                Page.CODEC.listOf().fieldOf("pages").forGetter { it.pages },
                Codec.FLOAT.fieldOf("titleScale").forGetter { it.titleScale }
            ).apply(instance, ::Book)
        }
    }
}
