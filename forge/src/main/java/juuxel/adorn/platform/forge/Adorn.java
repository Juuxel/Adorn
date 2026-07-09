package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlockSetTypes;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.AdornWoodTypes;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.group.AdornItemGroups;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.loot.AdornLootConditionTypes;
import juuxel.adorn.loot.AdornLootFunctionTypes;
import juuxel.adorn.menu.AdornMenus;
import juuxel.adorn.platform.forge.client.AdornClient;
import juuxel.adorn.platform.forge.compat.Compat;
import juuxel.adorn.platform.forge.event.BlockEvents;
import juuxel.adorn.platform.forge.event.EntityEvents;
import juuxel.adorn.platform.forge.event.ItemEvents;
import juuxel.adorn.platform.forge.networking.AdornNetworking;
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar;
import juuxel.adorn.recipe.AdornRecipeSerializers;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.util.Dyes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForgeMod;

@Mod(AdornCommon.NAMESPACE)
public final class Adorn {
    // TODO: Split into separate common and client @Mods
    public Adorn(IEventBus modBus, Dist dist) {
        ConfigManager.get().init();
        modBus.addListener(this::init);
        register(AdornComponentTypes.DATA_COMPONENT_TYPES, modBus);
        register(AdornSounds.SOUNDS, modBus);
        AdornBlockSetTypes.init();
        AdornWoodTypes.init();
        register(AdornBlocks.BLOCKS, modBus);
        register(AdornBlocks.ITEMS, modBus);
        register(AdornItems.ITEMS, modBus);
        register(AdornEntities.ENTITIES, modBus);
        register(AdornBlockEntities.BLOCK_ENTITIES, modBus);
        AdornItemGroups.init();
        register(AdornItemGroups.ITEM_GROUPS, modBus);
        AdornRecipeTypes.init();
        AdornRecipeSerializers.init();
        register(AdornMenus.MENUS, modBus);
        register(AdornRecipeSerializers.RECIPE_SERIALIZERS, modBus);
        register(AdornRecipeTypes.RECIPE_TYPES, modBus);
        register(AdornLootConditionTypes.LOOT_CONDITION_TYPES, modBus);
        register(AdornLootFunctionTypes.LOOT_FUNCTION_TYPES, modBus);
        register(AdornStats.CUSTOM_STATS, modBus);
        modBus.addListener(AdornNetworking::register);
        AdornCriteria.init();
        register(AdornCriteria.CRITERIA, modBus);
        ItemEvents.register(modBus);
        BlockEvents.init();
        EntityEvents.init();
        modBus.addListener(AdornCapabilities::register);
        Compat.init(modBus);
        BlockVariantSets.register();
        NeoForgeMod.enableMilkFluid();

        if (dist == Dist.CLIENT) {
            AdornClient.init(modBus);
        }

        if (!FMLEnvironment.production) {
            Dyes.checkInDev();
        }
    }

    private void register(Registrar<?> registrar, IEventBus modBus) {
        ((ForgeRegistrar<?>) registrar).hook(modBus);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(this::initOnMainThread);
        AdornTags.init();
        AdornStats.init();
        ConfigManager.get().finish();
    }

    private void initOnMainThread() {
        AdornGameRules.init();
    }
}
