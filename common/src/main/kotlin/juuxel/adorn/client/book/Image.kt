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
    val placement: Placement,
    val hoverAreas: List<HoverArea>
) {
    companion object {
        val CODEC: Codec<Image> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("location").forGetter { it.location },
                Vec2i.CODEC.fieldOf("size").forGetter { it.size },
                Placement.CODEC.optionalFieldOf("placement", Placement.AFTER_TEXT).forGetter { it.placement },
                HoverArea.CODEC.listOf().optionalFieldOf("hoverAreas", emptyList()).forGetter { it.hoverAreas }
            ).apply(instance, ::Image)
        }
    }

    enum class Placement(private val id: String) {
        BEFORE_TEXT("beforeText"),
        AFTER_TEXT("afterText");

        companion object {
            private val BY_ID: Map<String, Placement> = values().associateBy { it.id }
            val CODEC: Codec<Placement> = Codec.STRING.xmap(BY_ID::get, Placement::id)
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
