package juuxel.adorn.platform.forge;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.CommonEventHandlers;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.SneakClickHandler;
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
import net.minecraft.util.ActionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        forgeBus.addListener(this::handleCarpetedBlocks);
        forgeBus.addListener(this::handleSneakClicks);
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

    private void handleCarpetedBlocks(PlayerInteractEvent.RightClickBlock event) {
        ActionResult result = CommonEventHandlers.handleCarpets(event.getPlayer(), event.getWorld(), event.getHand(), event.getHitVec());

        if (result != ActionResult.PASS) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private void handleSneakClicks(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getPlayer();
        var state = event.getWorld().getBlockState(event.getPos());

        // Check that:
        // - the block is a sneak-click handler
        // - the player is sneaking
        // - the player isn't holding an item (for block item and bucket support)
        if (state.getBlock() instanceof SneakClickHandler clickHandler && player.isSneaking() && player.getStackInHand(event.getHand()).isEmpty()) {
            var result = clickHandler.onSneakClick(state, event.getWorld(), event.getPos(), player, event.getHand(), event.getHitVec());

            if (result != ActionResult.PASS) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }
    }
}
