package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import juuxel.adorn.fluid.FluidReference
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object AdornNetworking {
    val ENTITY_SPAWN = AdornCommon.id("entity_spawn")
    val OPEN_BOOK = AdornCommon.id("open_book")
    val BREWER_FLUID_SYNC = AdornCommon.id("brewer_fluid_sync")

    fun init() {
    }

    fun createEntitySpawnPacket(entity: Entity): Packet<*> =
        ServerPlayNetworking.createS2CPacket(
            ENTITY_SPAWN,
            PacketByteBufs.create().apply {
                EntitySpawnS2CPacket(entity).write(this)
            }
        )

    fun sendOpenBookPacket(player: PlayerEntity, bookId: Identifier) {
        if (player is ServerPlayerEntity) {
            val buf = PacketByteBufs.create()
            buf.writeIdentifier(bookId)
            ServerPlayNetworking.send(player, OPEN_BOOK, buf)
        }
    }

    fun sendBrewerFluidSync(player: PlayerEntity, syncId: Int, fluid: FluidReference) {
        if (player is ServerPlayerEntity) {
            val buf = PacketByteBufs.create()
            buf.writeByte(syncId)
            fluid.write(buf)
            ServerPlayNetworking.send(player, BREWER_FLUID_SYNC, buf)
        }
    }
}
