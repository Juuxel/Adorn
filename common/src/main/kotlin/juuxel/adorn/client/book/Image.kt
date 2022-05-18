package juuxel.adorn.client.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.MoreCodecs
import net.minecraft.text.Text
import net.minecraft.util.Identifier

data class Image(
    val location: Identifier,
    val width: Int,
    val height: Int,
    val verticalAlignment: VerticalAlignment,
    val hoverAreas: List<HoverArea>
) {
    companion object {
        val CODEC: Codec<Image> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("location").forGetter { it.location },
                Codec.INT.fieldOf("width").forGetter { it.width },
                Codec.INT.fieldOf("height").forGetter { it.height },
                VerticalAlignment.CODEC.optionalFieldOf("verticalAlignment", VerticalAlignment.CENTER).forGetter { it.verticalAlignment },
                HoverArea.CODEC.listOf().optionalFieldOf("hoverAreas", emptyList()).forGetter { it.hoverAreas }
            ).apply(instance, ::Image)
        }
    }

    enum class VerticalAlignment(private val id: String) {
        TOP("top"),
        CENTER("center"),
        BOTTOM("bottom");

        companion object {
            private val BY_ID: Map<String, VerticalAlignment> = values().associateBy { it.id }
            val CODEC: Codec<VerticalAlignment> = Codec.STRING.xmap(BY_ID::get, VerticalAlignment::id)
        }
    }

    data class HoverArea(val x: Int, val y: Int, val width: Int, val height: Int, val tooltip: Text) {
        fun contains(x: Int, y: Int): Boolean =
            x in this.x..(this.x + width) && y in this.y..(this.y + height)

        companion object {
            val CODEC: Codec<HoverArea> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.INT.fieldOf("x").forGetter { it.x },
                    Codec.INT.fieldOf("y").forGetter { it.y },
                    Codec.INT.fieldOf("width").forGetter { it.width },
                    Codec.INT.fieldOf("height").forGetter { it.height },
                    MoreCodecs.TEXT.fieldOf("tooltip").forGetter { it.tooltip }
                ).apply(instance, ::HoverArea)
            }
        }
    }
}
