package juuxel.adorn.platform.fabric

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceReloader
import net.minecraft.util.Identifier

class ResourceReloaderWithId(private val id: Identifier, reloader: ResourceReloader) : ResourceReloader by reloader, IdentifiableResourceReloadListener {
    override fun getFabricId(): Identifier = id
}
