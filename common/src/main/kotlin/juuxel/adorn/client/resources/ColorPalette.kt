package juuxel.adorn.client.resources

import com.google.common.collect.ImmutableMap
import juuxel.adorn.AdornCommon
import net.minecraft.util.Identifier

class ColorPalette(map: Map<Identifier, ColorManager.ColorPair>) {
    private val map = ImmutableMap.copyOf(map)

    operator fun get(key: Identifier): ColorManager.ColorPair =
        map.getOrElse(key) {
            map[DEFAULT_COLOR_ID] ?: error("Couldn't read default value from palette map")
        }

    companion object {
        private val DEFAULT_COLOR_ID = AdornCommon.id("default")
    }
}
