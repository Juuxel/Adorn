package juuxel.adorn;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlockSetTypes;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.AdornWoodTypes;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.compat.Compat;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.AdornTrackedDataHandlers;
import juuxel.adorn.event.BlockEvents;
import juuxel.adorn.event.EntityEvents;
import juuxel.adorn.event.ItemEvents;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.group.AdornItemGroups;
import juuxel.adorn.lib.AdornCapabilities;
import juuxel.adorn.lib.AdornDynamicRegistries;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.loot.AdornLootConditionTypes;
import juuxel.adorn.loot.AdornLootFunctionTypes;
import juuxel.adorn.menu.AdornMenus;
import juuxel.adorn.networking.AdornNetworking;
import juuxel.adorn.recipe.AdornRecipeBookCategories;
import juuxel.adorn.recipe.AdornRecipeDisplays;
import juuxel.adorn.recipe.AdornRecipeSerializers;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.recipe.AdornSlotDisplays;
import juuxel.adorn.registrar.NeoRegistrar;
import juuxel.adorn.util.Dyes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForgeMod;

@Mod(AdornCommon.NAMESPACE)
public final class Adorn {
    public Adorn(IEventBus modBus) {
        ConfigManager.get().init();
        modBus.addListener(this::init);
        register(AdornComponentTypes.DATA_COMPONENT_TYPES, modBus);
        register(AdornSounds.SOUNDS, modBus);
        AdornBlockSetTypes.init();
        AdornWoodTypes.init();
        register(AdornBlocks.BLOCKS, modBus);
        register(AdornBlocks.ITEMS, modBus);
        register(AdornItems.ITEMS, modBus);
        AdornTrackedDataHandlers.init();
        register(AdornTrackedDataHandlers.TRACKED_DATA_HANDLERS, modBus);
        register(AdornEntities.ENTITIES, modBus);
        register(AdornBlockEntities.BLOCK_ENTITIES, modBus);
        AdornItemGroups.init();
        register(AdornItemGroups.ITEM_GROUPS, modBus);
        AdornRecipeBookCategories.init();
        AdornRecipeTypes.init();
        AdornRecipeSerializers.init();
        AdornRecipeDisplays.init();
        AdornSlotDisplays.init();
        register(AdornMenus.MENUS, modBus);
        register(AdornRecipeBookCategories.RECIPE_BOOK_CATEGORIES, modBus);
        register(AdornRecipeSerializers.RECIPE_SERIALIZERS, modBus);
        register(AdornRecipeDisplays.RECIPE_DISPLAYS, modBus);
        register(AdornRecipeTypes.RECIPE_TYPES, modBus);
        register(AdornSlotDisplays.SLOT_DISPLAYS, modBus);
        register(AdornLootConditionTypes.LOOT_CONDITION_TYPES, modBus);
        register(AdornLootFunctionTypes.LOOT_FUNCTION_TYPES, modBus);
        register(AdornStats.CUSTOM_STATS, modBus);
        register(AdornGameRules.GAME_RULES, modBus);
        modBus.addListener(AdornNetworking::register);
        AdornCriteria.init();
        register(AdornCriteria.CRITERIA, modBus);
        ItemEvents.register(modBus);
        BlockEvents.init();
        EntityEvents.init();
        modBus.addListener(AdornCapabilities::register);
        modBus.addListener(AdornDynamicRegistries::register);
        Compat.init(modBus);
        BlockVariantSets.register();
        NeoForgeMod.enableMilkFluid();

        if (!FMLEnvironment.isProduction()) {
            Dyes.checkInDev();
        }
    }

    private void register(Registrar<?> registrar, IEventBus modBus) {
        ((NeoRegistrar<?>) registrar).hook(modBus);
    }

    private void init(FMLCommonSetupEvent event) {
        AdornTags.init();
        AdornStats.init();
        ConfigManager.get().finish();
    }
}
