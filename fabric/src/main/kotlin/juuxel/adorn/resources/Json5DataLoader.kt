package juuxel.adorn.resources

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonObject
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SinglePreparationResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

abstract class Json5DataLoader(
    private val id: Identifier, private val directory: String, protected val jankson: Jankson = Jankson.builder().build()
) : SinglePreparationResourceReloader<Map<Identifier, JsonObject>>(), IdentifiableResourceReloadListener {
    final override fun getFabricId() = id

    final override fun prepare(manager: ResourceManager, profiler: Profiler): Map<Identifier, JsonObject> {
        return manager.findResources(directory) { it.endsWith(".json5") }
            .associateWith { id ->
                manager.getResource(id).use { resource ->
                    resource.inputStream.use(jankson::load)
                }
            }
    }
}
