package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.NotNull;

public final class NetworkBridge {
    @ExpectPlatform
    public static void sendToTracking(Entity entity, Packet<?> packet) {
        PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static Packet<?> createEntitySpawnPacket(Entity entity) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    public static void syncBlockEntity(BlockEntity be) {
        PlatformCore.expected();
    }
}
