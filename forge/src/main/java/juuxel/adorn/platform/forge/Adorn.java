package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.FuelData;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.platform.Registrar;
import juuxel.adorn.platform.forge.client.AdornClient;
import juuxel.adorn.platform.forge.menu.AdornMenus;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
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

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        /* TODO:
        FORGE_BUS.addListener(AdornBlocks::handleSneakClicks)
        FORGE_BUS.addListener(AdornBlocks::handleCarpetedBlocks)
        */
        forgeBus.addListener(this::onFuelBurnTime);

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

    private void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        Item item = event.getItemStack().getItem();

        for (FuelData fuelData : FuelData.FUEL_DATA) {
            Class<? extends ItemConvertible> type = fuelData.itemOrBlockType();
            if (type == null) continue;

            if (type.isInstance(item) || (item instanceof BlockItem blockItem && type.isInstance(blockItem.getBlock()))) {
                event.setBurnTime(fuelData.burnTime());
                break;
            }
        }
    }
}
