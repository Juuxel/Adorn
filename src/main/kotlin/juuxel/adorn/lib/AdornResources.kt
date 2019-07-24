package juuxel.adorn.lib

import juuxel.adorn.resources.ColorManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object AdornResources {
    @Environment(EnvType.CLIENT)
    fun initClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(ColorManager)
    }
}
