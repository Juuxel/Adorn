package juuxel.adorn.client.resources;

import juuxel.adorn.AdornCommon;
import net.minecraft.resources.Identifier;

import java.util.Map;

public final class ColorPalette {
    private static final Identifier DEFAULT_COLOR_ID = AdornCommon.id("default");
    private final Map<Identifier, ColorManager.ColorPair> map;

    public ColorPalette(Map<Identifier, ColorManager.ColorPair> map) {
        this.map = Map.copyOf(map);
    }

    public ColorManager.ColorPair get(Identifier key) {
        var pair = map.get(key);
        if (pair == null) {
            pair = map.get(DEFAULT_COLOR_ID);
            if (pair == null) throw new IllegalStateException("Couldn't read default value from palette map");
        }
        return pair;
    }
}
