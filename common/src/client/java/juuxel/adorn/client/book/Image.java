package juuxel.adorn.client.book;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.Vec2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Image(Identifier location, Vec2i size, Placement placement, List<HoverArea> hoverAreas) {
    public static final Codec<Image> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Identifier.CODEC.fieldOf("location").forGetter(Image::location),
        Vec2i.CODEC.fieldOf("size").forGetter(Image::size),
        Placement.CODEC.optionalFieldOf("placement", Placement.AFTER_TEXT).forGetter(Image::placement),
        HoverArea.CODEC.listOf().optionalFieldOf("hoverAreas", List.of()).forGetter(Image::hoverAreas)
    ).apply(instance, Image::new));

    public enum Placement {
        BEFORE_TEXT("beforeText"),
        AFTER_TEXT("afterText");

        private static final Map<String, Placement> BY_ID = Arrays.stream(values())
            .map(placement -> Pair.of(placement.id, placement))
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        public static final Codec<Placement> CODEC = Codec.STRING.xmap(BY_ID::get, placement -> placement.id);

        private final String id;

        Placement(String id) {
            this.id = id;
        }
    }

    public record HoverArea(Vec2i position, Vec2i size, Component tooltip) {
        public static final Codec<HoverArea> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec2i.CODEC.fieldOf("position").forGetter(HoverArea::position),
            Vec2i.CODEC.fieldOf("size").forGetter(HoverArea::size),
            ComponentSerialization.CODEC.fieldOf("tooltip").forGetter(HoverArea::tooltip)
        ).apply(instance, HoverArea::new));

        public boolean contains(int x, int y) {
            return position.x() <= x && x <= position.x() + size.x() && position.y() <= y && y <= position.y() + size.y();
        }
    }
}
