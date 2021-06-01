package juuxel.adorn.platform.fabric;

import juuxel.adorn.lib.AdornNetworking;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;

public final class NetworkBridgeImpl {
    public static void sendToTracking(Entity entity, Packet<?> packet) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet);
        }
    }

    public static Packet<?> createEntitySpawnPacket(Entity entity) {
        return AdornNetworking.INSTANCE.createEntitySpawnPacket(entity);
    }

    public static void syncBlockEntity(BlockEntity be) {
        ((BlockEntityClientSerializable) be).sync();
    }
}
