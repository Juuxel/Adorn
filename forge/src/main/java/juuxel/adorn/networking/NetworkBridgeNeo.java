package juuxel.adorn.networking;

import juuxel.adorn.platform.NetworkBridge;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkBridgeNeo implements NetworkBridge {
    public static final NetworkBridgeNeo INSTANCE = new NetworkBridgeNeo();

    @Override
    public void sendToTracking(Entity entity, Packet<? super ClientGamePacketListener> packet) {
        if (entity.level().getChunkSource() instanceof ServerChunkCache chunkManager) {
            chunkManager.sendToTrackingPlayers(entity, packet);
        }
    }

    @Override
    public void sendToClient(Player player, CustomPacketPayload payload) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, payload);
        }
    }
}
