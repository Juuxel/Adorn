package juuxel.adorn.resources

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.resources.BlockVariantTextureLoader
import juuxel.adorn.client.resources.BookManagerFabric
import juuxel.adorn.client.resources.ColorManagerFabric
import juuxel.adorn.platform.fabric.ResourceReloaderWithId
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

object AdornResources {
    @Environment(EnvType.CLIENT)
    fun initClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).apply {
            registerReloadListener(ColorManagerFabric)
            registerReloadListener(BookManagerFabric)
            registerReloadListener(ResourceReloaderWithId(AdornCommon.id("block_variant_textures"), BlockVariantTextureLoader))
        }
    }
}
