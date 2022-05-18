package juuxel.adorn.client.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.util.MoreCodecs
import juuxel.adorn.util.Vec2i
import net.minecraft.text.Text
import net.minecraft.util.Identifier

data class Image(
    val location: Identifier,
    val size: Vec2i,
    val verticalAlignment: VerticalAlignment,
    val hoverAreas: List<HoverArea>
) {
    companion object {
        val CODEC: Codec<Image> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("location").forGetter { it.location },
                Vec2i.CODEC.fieldOf("size").forGetter { it.size },
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

    data class HoverArea(val position: Vec2i, val size: Vec2i, val tooltip: Text) {
        fun contains(x: Int, y: Int): Boolean =
            x in position.x..(position.x + size.x) && y in position.y..(position.y + size.y)

        companion object {
            val CODEC: Codec<HoverArea> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Vec2i.CODEC.fieldOf("position").forGetter { it.position },
                    Vec2i.CODEC.fieldOf("size").forGetter { it.size },
                    MoreCodecs.TEXT.fieldOf("tooltip").forGetter { it.tooltip }
                ).apply(instance, ::HoverArea)
            }
        }
    }
}
