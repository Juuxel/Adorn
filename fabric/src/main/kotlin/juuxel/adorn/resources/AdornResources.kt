package juuxel.adorn.resources

import juuxel.adorn.client.resources.BookManager
import juuxel.adorn.client.resources.ColorManagerFabric
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object AdornResources {
    @Environment(EnvType.CLIENT)
    fun initClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).apply {
            registerReloadListener(ColorManagerFabric)
            registerReloadListener(BookManager)
        }
    }
}
