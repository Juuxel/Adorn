package juuxel.adorn.platform.forge

import juuxel.adorn.platform.NetworkBridge
import net.minecraft.entity.Entity
import net.minecraft.network.Packet
import net.minecraftforge.network.NetworkHooks
import net.minecraftforge.network.PacketDistributor

object NetworkBridgeImpl : NetworkBridge {
    override fun sendToTracking(entity: Entity, packet: Packet<*>) =
        PacketDistributor.TRACKING_ENTITY.with { entity }.send(packet)

    override fun createEntitySpawnPacket(entity: Entity): Packet<*> =
        NetworkHooks.getEntitySpawningPacket(entity)
}
