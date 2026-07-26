package juuxel.adorn.util.animation;

import juuxel.adorn.util.Colors;
import net.minecraft.util.Mth;

@FunctionalInterface
public interface Interpolator<T> {
    Interpolator<Float> FLOAT = Mth::lerp;
    Interpolator<Double> DOUBLE = Mth::lerp;
    Interpolator<Integer> COLOR = (delta, from, to) -> {
        float alpha = FLOAT.interpolate(delta, Colors.alphaOf(from), Colors.alphaOf(to));
        float red = FLOAT.interpolate(delta, Colors.redOf(from), Colors.redOf(to));
        float green = FLOAT.interpolate(delta, Colors.greenOf(from), Colors.greenOf(to));
        float blue = FLOAT.interpolate(delta, Colors.blueOf(from), Colors.blueOf(to));
        return Colors.color(red, green, blue, alpha);
    };

    T interpolate(float delta, T from, T to);
}
