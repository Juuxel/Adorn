package juuxel.adorn

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.compat.Compat
import juuxel.adorn.criterion.AdornCriteria
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.AdornBlocksFabric
import juuxel.adorn.lib.AdornGameRules
import juuxel.adorn.lib.AdornItemsFabric
import juuxel.adorn.lib.AdornNetworking
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.lib.SofaSleeping
import juuxel.adorn.loot.AdornLootConditionTypes
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.platform.fabric.ConfigManagerImpl
import juuxel.adorn.recipe.AdornRecipes
import net.fabricmc.api.ModInitializer

object Adorn : ModInitializer {
    override fun onInitialize() {
        ConfigManagerImpl.init()
        AdornSounds.init()
        AdornBlocks.init()
        AdornBlocksFabric.init()
        AdornBlockEntities.init()
        AdornItems.init()
        AdornItemsFabric.init()
        AdornEntities.init()
        AdornMenus.init()
        AdornNetworking.init()
        AdornTags.init()
        AdornGameRules.init()
        AdornStats.init()
        SofaSleeping.init()
        AdornCriteria.init()
        AdornRecipes.init()
        AdornLootConditionTypes.init()
        Compat.init()
        ConfigManagerImpl.finalize()
    }
}
