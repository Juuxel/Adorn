package juuxel.adorn.platform.forge;

import juuxel.adorn.platform.NetworkBridge;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerChunkCache;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

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
