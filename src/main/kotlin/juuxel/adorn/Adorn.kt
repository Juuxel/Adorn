package juuxel.adorn

import juuxel.adorn.lib.ModBlocks
import juuxel.adorn.lib.ModGuis
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        ModBlocks.init()
        ModGuis.init()
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        ModGuis.initClient()
    }
}
