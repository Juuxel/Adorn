package juuxel.adorn.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;

import java.util.stream.IntStream;

public record Vec2i(int x, int y) {
    public static final Codec<Vec2i> CODEC = Codec.INT_STREAM.comapFlatMap(
        stream -> Util.fixedSize(stream, 2).map(array -> new Vec2i(array[0], array[1])),
        vec -> IntStream.of(vec.x, vec.y)
    );
}
