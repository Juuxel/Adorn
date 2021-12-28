package juuxel.adorn.platform.forge

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.forge.client.AdornClient
import juuxel.adorn.platform.forge.networking.AdornNetworking
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.DistExecutor.SafeRunnable
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AdornCommon.NAMESPACE)
object Adorn {
    init {
        PlatformBridges.configManager.init()
        MOD_BUS.addListener(this::init)
        EventsImplementedInJava().register(MOD_BUS, FORGE_BUS)
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable(AdornClient::init) }
        AdornNetworking.init()
        // TODO: Compat.init(MOD_BUS)
    }

    private fun init(event: FMLCommonSetupEvent) {
        AdornStats.init()
    }
}
