package juuxel.adorn.platform;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.world.ServerWorld;

public interface NetworkBridge {
    void sendToTracking(Entity entity, Packet<? super ClientPlayPacketListener> packet);
    void sendToClient(PlayerEntity player, CustomPayload payload);

    default void syncBlockEntity(BlockEntity be) {
        if (!(be.getWorld() instanceof ServerWorld world)) {
            throw new IllegalStateException("[Adorn] Block entities cannot be synced client->server");
        }
        world.getChunkManager().markForUpdate(be.getPos());
    }
}
