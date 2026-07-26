package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.NetworkBridge;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

public final class NetworkBridgeImpl implements NetworkBridge {
    public static final NetworkBridgeImpl INSTANCE = new NetworkBridgeImpl();

    @Override
    public void sendToTracking(Entity entity, Packet<? super ClientGamePacketListener> packet) {
        for (var player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet);
        }
    }

    @Override
    public void sendToClient(Player player, CustomPacketPayload payload) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, payload);
        }
    }
}
