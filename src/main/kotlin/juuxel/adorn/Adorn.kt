package juuxel.adorn

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.config.AdornGameRules
import juuxel.adorn.config.Config
import juuxel.adorn.entity.AdornEntities
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.AdornItemTags
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.menu.AdornMenus
import net.minecraft.util.Identifier
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Adorn.NAMESPACE)
object Adorn {
    const val NAMESPACE = "adorn"

    init {
        Config.load(FMLPaths.CONFIGDIR.get().resolve("Adorn.toml"))

        AdornSounds.SOUNDS.register(MOD_BUS)
        AdornBlocks.BLOCKS.register(MOD_BUS)
        AdornBlocks.ITEMS.register(MOD_BUS)
        AdornItems.ITEMS.register(MOD_BUS)
        AdornMenus.MENUS.register(MOD_BUS)
        AdornEntities.ENTITIES.register(MOD_BUS)
        AdornBlockEntities.BLOCK_ENTITIES.register(MOD_BUS)
        MOD_BUS.addListener(this::init)

        FORGE_BUS.addListener(AdornBlocks::handleSneakClicks)
        FORGE_BUS.addListener(AdornBlocks::handleCarpetedBlocks)

        DistExecutor.safeRunWhenOn(Dist.CLIENT) {
            DistExecutor.SafeRunnable {
                MOD_BUS.addListener(AdornBlocks::registerColorProviders)
                MOD_BUS.addListener(this::initClient)
            }
        }
    }

    private fun init(event: FMLCommonSetupEvent) {
        AdornGameRules.init()
        AdornItemTags.init()
    }

    @OnlyIn(Dist.CLIENT)
    private fun initClient(event: FMLClientSetupEvent) {
        AdornBlocks.initClient()
        AdornEntities.initClient()
        AdornMenus.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)

    fun <A : IForgeRegistryEntry<A>> register(registry: IForgeRegistry<A>): DeferredRegister<A> =
        DeferredRegister.create(registry, NAMESPACE)
}
