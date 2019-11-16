package juuxel.adorn

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.compat.AdornCompat
import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.debug.Debug
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.AdornNetworking
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.part.AdornParts
import juuxel.adorn.resources.AdornResources
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        AdornConfigManager.init()
        AdornBlocks.init()
        AdornItems.init()
        AdornEntities.init()
        AdornGuis.init()
        AdornNetworking.init()
        AdornTags.init()
        AdornParts.init()
        AdornCompat.init()

        if (Debug.shouldLoad()) {
            Debug.init()
        }
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        AdornBlocks.initClient()
        AdornItems.initClient()
        AdornGuis.initClient()
        AdornNetworking.initClient()
        AdornResources.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)
}
