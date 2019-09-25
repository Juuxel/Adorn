package juuxel.adorn.resources

import blue.endless.jankson.JsonObject
import juuxel.adorn.Adorn
import juuxel.adorn.guide.Guide
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

object GuideManager : Json5DataLoader(Adorn.id("guide_manager"), "guides", Guide.jankson().build()), IdentifiableResourceReloadListener {
    private const val PREFIX_LENGTH = "guides".length
    private const val SUFFIX_LENGTH = ".json5".length
    private var guides: Map<Identifier, Guide> = emptyMap()

    override fun apply(jsons: Map<Identifier, JsonObject>, manager: ResourceManager, profiler: Profiler) {
        guides = jsons.asSequence()
            .map { (key, value) ->
                Identifier(key.namespace, key.path.substring(PREFIX_LENGTH + 1, key.path.length - SUFFIX_LENGTH)) to value
            }.map { (key, value) ->
                key to jankson.fromJson(value, Guide::class.java)
            }.toMap()
    }

    operator fun contains(id: Identifier): Boolean = guides.contains(id)

    operator fun get(id: Identifier): Guide =
        guides.getOrElse(id) { throw IllegalArgumentException("Tried to get unknown guide '$id' from GuideManager") }
}
