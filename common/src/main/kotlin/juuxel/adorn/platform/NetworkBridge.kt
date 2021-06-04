package juuxel.adorn.platform

import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.network.Packet

interface NetworkBridge {
    fun sendToTracking(entity: Entity, packet: Packet<*>)
    fun createEntitySpawnPacket(entity: Entity): Packet<*>
    fun syncBlockEntity(be: BlockEntity)
}
