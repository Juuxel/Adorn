package juuxel.adorn

import juuxel.adorn.lib.ModBlocks
import juuxel.adorn.lib.ModGuis
import net.fabricmc.api.ClientModInitializer
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
    object Client : ClientModInitializer {
        override fun onInitializeClient() {
            ModGuis.initClient()
        }
    }
}
