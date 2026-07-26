package juuxel.adorn.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;

public interface NetworkBridge {
    void sendToTracking(Entity entity, Packet<? super ClientGamePacketListener> packet);
    void sendToClient(Player player, CustomPacketPayload payload);

    default void syncBlockEntity(BlockEntity be) {
        if (!(be.getLevel() instanceof ServerLevel world)) {
            throw new IllegalStateException("[Adorn] Block entities cannot be synced client->server");
        }
        world.getChunkSource().blockChanged(be.getBlockPos());
    }
}
