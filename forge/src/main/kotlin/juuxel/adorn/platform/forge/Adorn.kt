package juuxel.adorn.platform.forge

import juuxel.adorn.AdornCommon
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.criterion.AdornCriteria
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.loot.AdornLootConditionTypes
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.forge.client.AdornClient
import juuxel.adorn.platform.forge.compat.Compat
import juuxel.adorn.platform.forge.event.ItemEvents
import juuxel.adorn.platform.forge.networking.AdornNetworking
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar
import juuxel.adorn.recipe.AdornRecipes
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeMod
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
        ConfigManager.INSTANCE.init()
        MOD_BUS.addListener(this::init)
        EventsImplementedInJava().register(MOD_BUS, FORGE_BUS)
        AdornRecipes.init()
        AdornMenus.MENUS.registerToBus(MOD_BUS)
        AdornRecipes.RECIPE_SERIALIZERS.registerToBus(MOD_BUS)
        AdornRecipes.RECIPE_TYPES.registerToBus(MOD_BUS)
        AdornLootConditionTypes.LOOT_CONDITION_TYPES.registerToBus(MOD_BUS)
        AdornNetworking.init()
        AdornCriteria.init()
        ItemEvents.register(FORGE_BUS)
        Compat.init(MOD_BUS)
        ForgeMod.enableMilkFluid()
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable(AdornClient::init) }
    }

    private fun Registrar<*>.registerToBus(modBus: IEventBus) =
        (this as ForgeRegistrar<*>).hook(modBus)

    private fun init(event: FMLCommonSetupEvent) {
        AdornStats.init()
        ConfigManager.INSTANCE.finalize()
    }
}
