package juuxel.adorn.platform.fabric

import juuxel.adorn.lib.AdornNetworking
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.network.Packet

object NetworkBridgeImpl {
    @JvmStatic
    fun sendToTracking(entity: Entity, packet: Packet<*>) {
        for (player in PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet)
        }
    }

    @JvmStatic
    fun createEntitySpawnPacket(entity: Entity) = AdornNetworking.createEntitySpawnPacket(entity)

    @JvmStatic
    fun syncBlockEntity(be: BlockEntity) = (be as BlockEntityClientSerializable).sync()
}
