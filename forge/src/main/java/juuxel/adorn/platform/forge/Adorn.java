package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.platform.Registrar;
import juuxel.adorn.platform.forge.client.AdornClient;
import juuxel.adorn.platform.forge.menu.AdornMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdornCommon.NAMESPACE)
public class Adorn {
    public Adorn() {
        // TODO: Config.load(FMLPaths.CONFIGDIR.get().resolve("Adorn.toml"))

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        register(AdornSounds.SOUNDS, modBus);
        register(AdornBlocks.BLOCKS, modBus);
        register(AdornBlocks.ITEMS, modBus);
        register(AdornItems.ITEMS, modBus);
        register(AdornMenus.MENUS, modBus);
        register(AdornEntities.ENTITIES, modBus);
        register(AdornBlockEntities.BLOCK_ENTITIES, modBus);
        modBus.addListener(this::init);

        /* TODO:
        FORGE_BUS.addListener(AdornBlocks::handleSneakClicks)
        FORGE_BUS.addListener(AdornBlocks::handleCarpetedBlocks)
        */

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> AdornClient::init);

        // TODO: Compat.init(MOD_BUS)
    }

    private void init(FMLCommonSetupEvent event) {
        AdornGameRules.init();
        AdornTags.init();
    }

    private void register(Registrar<?> registrar, IEventBus modBus) {
        ((RegistrarImpl<?>) registrar).getRegister().register(modBus);
    }
}
