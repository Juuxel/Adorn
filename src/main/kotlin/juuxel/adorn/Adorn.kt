package juuxel.adorn

import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.lib.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        AdornConfigManager.init()
        ModBlocks.init()
        ModItems.init()
        ModEntities.init()
        ModGuis.init()
        ModTags.init()
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        ModBlocks.initClient()
        ModGuis.initClient()
        ModNetworking.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)
}
