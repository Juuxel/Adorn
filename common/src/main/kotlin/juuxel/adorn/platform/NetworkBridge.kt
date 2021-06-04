package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.network.Packet

object NetworkBridge {
    @JvmStatic
    @ExpectPlatform
    fun sendToTracking(entity: Entity, packet: Packet<*>): Unit = expected

    @JvmStatic
    @ExpectPlatform
    fun createEntitySpawnPacket(entity: Entity): Packet<*> = expected

    @JvmStatic
    @ExpectPlatform
    fun syncBlockEntity(be: BlockEntity): Unit = expected
}
