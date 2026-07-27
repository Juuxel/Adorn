package juuxel.adorn.event;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSetSpawnEvent;

public final class EntityEvents {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(EntityEvents::preventSofaSpawns);
        NeoForge.EVENT_BUS.addListener(EntityEvents::removeSeatWhenLoggingOut);
    }

    private static void preventSofaSpawns(PlayerSetSpawnEvent event) {
        BlockPos pos = event.getNewSpawn();

        if (pos != null) {
            if (!event.isForced() && event.getEntity().level().getBlockState(pos).getBlock() instanceof SofaBlock) {
                event.setCanceled(true);
            }
        }
    }

    private static void removeSeatWhenLoggingOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        SeatEntity.stopSitting(player);
    }
}
