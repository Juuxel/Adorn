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
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.group.AdornItemGroups;
import juuxel.adorn.lib.AdornBlocksFabric;
import juuxel.adorn.lib.AdornEntitiesFabric;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornItemsFabric;
import juuxel.adorn.lib.AdornNetworking;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.SofaSleeping;
import juuxel.adorn.lib.registry.AdornDynamicRegistries;
import juuxel.adorn.loot.AdornLootConditionTypes;
import juuxel.adorn.loot.AdornLootFunctionTypes;
import juuxel.adorn.menu.AdornMenus;
import juuxel.adorn.recipe.AdornRecipeBookCategories;
import juuxel.adorn.recipe.AdornRecipeDisplays;
import juuxel.adorn.recipe.AdornRecipeSerializers;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.recipe.AdornSlotDisplays;
import juuxel.adorn.util.Dyes;
import net.fabricmc.loader.api.FabricLoader;

public final class Adorn {
    public static void init() {
        ConfigManager.get().init();
        AdornComponentTypes.init();
        AdornDynamicRegistries.init();
        AdornSounds.init();
        AdornBlockSetTypes.init();
        AdornWoodTypes.init();
        AdornBlocks.init();
        AdornBlocksFabric.init();
        AdornBlockEntities.init();
        AdornItems.init();
        AdornItemsFabric.init();
        AdornItemGroups.init();
        AdornTrackedDataHandlers.init();
        AdornEntities.init();
        AdornEntitiesFabric.init();
        AdornMenus.init();
        AdornNetworking.init();
        AdornTags.init();
        AdornGameRules.init();
        AdornStats.init();
        SofaSleeping.init();
        AdornCriteria.init();
        AdornRecipeBookCategories.init();
        AdornRecipeTypes.init();
        AdornRecipeSerializers.init();
        AdornRecipeDisplays.init();
        AdornSlotDisplays.init();
        AdornLootConditionTypes.init();
        AdornLootFunctionTypes.init();
        Compat.init();
        BlockVariantSets.register();
        AdornBlocksFabric.afterRegister();
        ConfigManager.get().finish();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Dyes.checkInDev();
        } else {
            Dyes.checkInProduction();
        }
    }
}
