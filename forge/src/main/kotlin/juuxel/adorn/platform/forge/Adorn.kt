package juuxel.adorn.platform.forge

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.criterion.AdornCriteria
import juuxel.adorn.item.group.AdornItemGroups
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.loot.AdornLootConditionTypes
import juuxel.adorn.loot.AdornLootFunctionTypes
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.platform.forge.client.AdornClient
import juuxel.adorn.platform.forge.compat.Compat
import juuxel.adorn.platform.forge.event.ItemEvents
import juuxel.adorn.platform.forge.networking.AdornNetworking
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar
import juuxel.adorn.recipe.AdornRecipes
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.DistExecutor
import net.neoforged.fml.DistExecutor.SafeRunnable
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod

@Mod(AdornCommon.NAMESPACE)
class Adorn {
    init {
        MOD_BUS = ModLoadingContext.get().activeContainer.eventBus!!
        ConfigManager.INSTANCE.init()
        MOD_BUS.addListener(this::init)
        EventsImplementedInJava().register(MOD_BUS)
        AdornItemGroups.init()
        AdornItemGroups.ITEM_GROUPS.registerToBus(MOD_BUS)
        AdornRecipes.init()
        AdornMenus.MENUS.registerToBus(MOD_BUS)
        AdornRecipes.RECIPE_SERIALIZERS.registerToBus(MOD_BUS)
        AdornRecipes.RECIPE_TYPES.registerToBus(MOD_BUS)
        AdornLootConditionTypes.LOOT_CONDITION_TYPES.registerToBus(MOD_BUS)
        AdornLootFunctionTypes.LOOT_FUNCTION_TYPES.registerToBus(MOD_BUS)
        MOD_BUS.register(AdornNetworking)
        AdornCriteria.init()
        AdornCriteria.CRITERIA.registerToBus(MOD_BUS)
        ItemEvents.register(MOD_BUS)
        MOD_BUS.register(AdornCapabilities)
        Compat.init(MOD_BUS)
        BlockVariantSets.register()
        NeoForgeMod.enableMilkFluid()
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable(AdornClient::init) }
    }

    private fun Registrar<*>.registerToBus(modBus: IEventBus) =
        (this as ForgeRegistrar<*>).hook(modBus)

    private fun init(event: FMLCommonSetupEvent) {
        AdornStats.init()
        ConfigManager.INSTANCE.finalize()
    }

    companion object {
        lateinit var MOD_BUS: IEventBus
    }
}
