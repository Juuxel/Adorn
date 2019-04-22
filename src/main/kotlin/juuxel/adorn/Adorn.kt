package juuxel.adorn

import juuxel.adorn.lib.ModBlocks
import net.fabricmc.api.ModInitializer

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        ModBlocks.init()
    }
}
