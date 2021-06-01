package juuxel.adorn

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.lib.AdornBlocksFabric
import juuxel.adorn.compat.Compat
import juuxel.adorn.config.AdornGameRules
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.AdornEntitiesFabric
import juuxel.adorn.lib.AdornItemsFabric
import juuxel.adorn.lib.AdornNetworking
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.resources.AdornResources
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer

object Adorn : ModInitializer {
    override fun onInitialize() {
        ConfigManager.init()
        AdornSounds.init()
        AdornBlocks.init()
        AdornBlocksFabric.init()
        AdornBlockEntities.init()
        AdornItems.init()
        AdornItemsFabric.init()
        AdornEntities.init()
        AdornMenus.init()
        AdornNetworking.init()
        AdornTags.init()
        AdornGameRules.init()
        Compat.init()
        ConfigManager.finalize()
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        AdornBlocksFabric.initClient()
        AdornEntitiesFabric.initClient()
        AdornMenus.initClient()
        AdornNetworking.initClient()
        AdornResources.initClient()
    }
}
