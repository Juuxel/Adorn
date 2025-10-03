package juuxel.adorn.platform.forge;

import juuxel.adorn.platform.NetworkBridge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

    @Override
    public void sendToTracking(Entity entity, Packet<? super ClientPlayPacketListener> packet) {
        if (entity.getEntityWorld().getChunkManager() instanceof ServerChunkManager chunkManager) {
            chunkManager.sendToOtherNearbyPlayers(entity, packet);
        }
    }

    @Override
    public void sendToClient(PlayerEntity player, CustomPayload payload) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, payload);
        }
    }
}
