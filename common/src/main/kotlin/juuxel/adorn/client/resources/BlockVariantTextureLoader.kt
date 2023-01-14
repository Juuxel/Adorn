package juuxel.adorn.client.resources

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import net.minecraft.resource.ResourceFinder
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SinglePreparationResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.profiler.Profiler

object BlockVariantTextureLoader : SinglePreparationResourceReloader<Map<Identifier, List<JsonObject>>>() {
    private const val DIRECTORY = "adorn/block_variant_textures"
    private val textures: MutableMap<Identifier, BlockVariantTextures> = HashMap()

    fun get(id: Identifier): BlockVariantTextures? = textures[id]

    override fun prepare(manager: ResourceManager, profiler: Profiler): Map<Identifier, List<JsonObject>> =
        ResourceFinder.json(DIRECTORY).findAllResources(manager)
            .mapValues { (_, resources) ->
                resources.map { resource ->
                    resource.reader.use { reader ->
                        JsonHelper.deserialize(reader)
                    }
                }
            }

    override fun apply(prepared: Map<Identifier, List<JsonObject>>, manager: ResourceManager, profiler: Profiler) {
        textures.clear()

        for ((_, jsons) in prepared) {
            for (json in jsons) {
                for (key in json.keySet()) {
                    val id = try {
                        Identifier(key)
                    } catch (e: Exception) {
                        throw JsonParseException("Could not parse key '$key' as an identifier", e)
                    }

                    textures[id] = BlockVariantTextures.fromJson(JsonHelper.getObject(json, key))
                }
            }
        }
    }
}
