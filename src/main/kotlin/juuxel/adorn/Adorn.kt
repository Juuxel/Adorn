package juuxel.adorn

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.compat.AdornCompat
import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.debug.Debug
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.AdornNetworking
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.resources.AdornResources
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        AdornConfigManager.init()
        AdornSounds.init()
        AdornBlocks.init()
        AdornBlockEntities.init()
        AdornItems.init()
        AdornEntities.init()
        AdornGuis.init()
        AdornNetworking.init()
        AdornTags.init()
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
        AdornEntities.initClient()
        AdornGuis.initClient()
        AdornNetworking.initClient()
        AdornResources.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)
}
