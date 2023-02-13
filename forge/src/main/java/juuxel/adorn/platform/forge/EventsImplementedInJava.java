package juuxel.adorn.platform.forge;

import juuxel.adorn.CommonEventHandlers;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.SneakClickHandler;
import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

// If you're wondering *why* these are implemented in Java,
// you can thank Forge for not initially having lang adapters on 1.17.
// This was the mod class.
final class EventsImplementedInJava {
    void register(IEventBus modBus, IEventBus forgeBus) {
        register(AdornSounds.SOUNDS, modBus);
        register(AdornBlocks.INSTANCE.getBlocks(), modBus);
        register(AdornBlocks.INSTANCE.getItems(), modBus);
        register(AdornItems.ITEMS, modBus);
        register(AdornEntities.ENTITIES, modBus);
        register(AdornBlockEntities.BLOCK_ENTITIES, modBus);
        modBus.addListener(this::init);

        forgeBus.addListener(this::handleCarpetedBlocks);
        forgeBus.addListener(this::handleSneakClicks);
        forgeBus.addListener(this::handleSofaSleepTime);
        forgeBus.addListener(this::preventSofaSpawns);
    }

    private void init(FMLCommonSetupEvent event) {
        AdornGameRules.init();
        AdornTags.init();
    }

    private void register(Registrar<?> registrar, IEventBus modBus) {
        ((ForgeRegistrar<?>) registrar).hook(modBus);
    }

    private void handleCarpetedBlocks(PlayerInteractEvent.RightClickBlock event) {
        ActionResult result = CommonEventHandlers.handleCarpets(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());

        if (result != ActionResult.PASS) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private void handleSneakClicks(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        var state = event.getLevel().getBlockState(event.getPos());

        // Check that:
        // - the block is a sneak-click handler
        // - the player is sneaking
        // - the player isn't holding an item (for block item and bucket support)
        if (state.getBlock() instanceof SneakClickHandler clickHandler && player.isSneaking() && player.getStackInHand(event.getHand()).isEmpty()) {
            var result = clickHandler.onSneakClick(state, event.getLevel(), event.getPos(), player, event.getHand(), event.getHitVec());

            if (result != ActionResult.PASS) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }
    }

    private void handleSofaSleepTime(SleepingTimeCheckEvent event) {
        BlockPos sleepingPos = event.getSleepingLocation().orElse(null);
        if (sleepingPos == null) return;
        World world = event.getEntity().world;

        if (world.isDay() && world.getBlockState(sleepingPos).getBlock() instanceof SofaBlock) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    private void preventSofaSpawns(PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();

        if (pos != null) {
            if (!event.isForced() && event.getEntity().world.getBlockState(pos).getBlock() instanceof SofaBlock) {
                event.setCanceled(true);
            }
        }
    }
}
