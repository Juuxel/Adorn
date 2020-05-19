package juuxel.adorn.client.resources

import com.google.common.collect.ImmutableMap
import juuxel.adorn.Adorn
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class ColorPalette(map: Map<Identifier, ColorManager.ColorPair>) {
    private val map = ImmutableMap.copyOf(map)

    operator fun get(key: Identifier): ColorManager.ColorPair =
        map.getOrElse(key) {
            map[DEFAULT_COLOR_ID] ?: error("Couldn't read default value from palette map")
        }

    companion object {
        private val DEFAULT_COLOR_ID = Adorn.id("default")
    }
}
