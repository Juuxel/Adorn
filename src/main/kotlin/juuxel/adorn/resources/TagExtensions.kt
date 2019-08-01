package juuxel.adorn.resources

import com.google.gson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.function.Function

object TagExtensions {
    private val LOGGER = LogManager.getLogger("Adorn|TagExtensions")

    @JvmStatic
    fun <T> load(getter: Function<Identifier, Optional<T>>, json: JsonObject): List<Tag.Entry<T>> {
        val result = ArrayList<Tag.Entry<T>>()

        for ((key, array) in json.entrySet()) {
            val condition = FabricLoader.getInstance().isModLoaded(key)
            if (!condition) continue

            for (valueElement in JsonHelper.asArray(array, key)) {
                val valueStr = JsonHelper.asString(valueElement, "value")

                if (valueStr.startsWith('#')) {
                    result += Tag.TagEntry(Identifier(valueStr.substring(1)))
                } else {
                    val entry = getter.apply(Identifier(valueStr)).orElse(null)
                    if (entry == null) {
                        LOGGER.warn("Unknown value '$valueStr' in Adorn tag extensions")
                        continue
                    }

                    result += Tag.CollectionEntry(setOf(entry))
                }
            }
        }

        return result
    }
}
