package juuxel.adorn.client.lib

import juuxel.adorn.resources.BookManagerFabric
import juuxel.adorn.resources.ColorManagerFabric
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object AdornResources {
    fun init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).apply {
            registerReloadListener(ColorManagerFabric)
            registerReloadListener(BookManagerFabric)
        }
    }
}
