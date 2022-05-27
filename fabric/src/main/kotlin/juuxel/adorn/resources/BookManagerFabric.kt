package juuxel.adorn.resources

import juuxel.adorn.AdornCommon
import juuxel.adorn.item.book.BookManager
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.util.Identifier

object BookManagerFabric : BookManager(), IdentifiableResourceReloadListener {
    private val ID = AdornCommon.id("book_manager")

    override fun getFabricId(): Identifier = ID
}
