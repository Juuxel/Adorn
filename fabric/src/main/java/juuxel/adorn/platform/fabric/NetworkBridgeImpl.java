package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.NetworkBridge;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

    @Override
    public void sendToTracking(Entity entity, Packet<? super ClientPlayPacketListener> packet) {
        for (var player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet);
        }
    }

    @Override
    public void sendToClient(PlayerEntity player, CustomPayload payload) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, payload);
        }
    }
}
