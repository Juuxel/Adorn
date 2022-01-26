package juuxel.adorn.platform.forge

import juuxel.adorn.AdornCommon
import juuxel.adorn.CommonEventHandlers
import juuxel.adorn.criterion.AdornCriteria
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.forge.client.AdornClient
import juuxel.adorn.platform.forge.compat.Compat
import juuxel.adorn.platform.forge.networking.AdornNetworking
import juuxel.adorn.recipe.AdornRecipes
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.IEventBus
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
        FORGE_BUS.addListener(this::onBlockAttack)
        EventsImplementedInJava().register(MOD_BUS, FORGE_BUS)
        AdornRecipes.init()
        AdornRecipes.RECIPE_SERIALIZERS.registerToBus(MOD_BUS)
        AdornNetworking.init()
        AdornCriteria.init()
        Compat.init(MOD_BUS)
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable(AdornClient::init) }
    }

    private fun Registrar<*>.registerToBus(modBus: IEventBus) =
        (this as RegistrarImpl<*>).register.register(modBus)

    private fun init(event: FMLCommonSetupEvent) {
        AdornStats.init()
        PlatformBridges.configManager.finalize()
    }

    private fun onBlockAttack(event: PlayerInteractEvent.LeftClickBlock) {
        if (CommonEventHandlers.shouldCancelBlockBreak(event.player, event.pos)) {
            event.isCanceled = true
        }
    }
}
