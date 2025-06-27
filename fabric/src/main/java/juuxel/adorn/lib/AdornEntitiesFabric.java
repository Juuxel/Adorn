package juuxel.adorn.lib;

import juuxel.adorn.entity.SeatEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public final class AdornEntitiesFabric {
    public static void init() {
        ServerPlayerEvents.LEAVE.register(SeatEntity::stopSitting);
    }
}
