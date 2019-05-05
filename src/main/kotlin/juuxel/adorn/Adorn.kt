package juuxel.adorn

import juuxel.adorn.lib.ModBlocks
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.lib.ModTags
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"

    override fun onInitialize() {
        ModBlocks.init()
        ModGuis.init()
        ModTags.init()
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        ModBlocks.initClient()
        ModGuis.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)
}
