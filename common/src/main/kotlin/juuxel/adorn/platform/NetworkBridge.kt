package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidReference
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

interface NetworkBridge {
    fun sendToTracking(entity: Entity, packet: Packet<*>)
    fun createEntitySpawnPacket(entity: Entity): Packet<*>
    fun sendOpenBookPacket(player: PlayerEntity, bookId: Identifier)
    fun sendBrewerFluidSync(player: PlayerEntity, syncId: Int, fluid: FluidReference)

    fun syncBlockEntity(be: BlockEntity) {
        // Logic taken from Fabric API's BlockEntityClientSerializable.sync()
        val world = be.world
        if (world !is ServerWorld) throw IllegalStateException("[Adorn] Block entities cannot be synced client->server")
        world.chunkManager.markForUpdate(be.pos)
    }
}
