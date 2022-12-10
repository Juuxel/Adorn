package juuxel.adorn.platform.forge

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.platform.NetworkBridge
import juuxel.adorn.platform.forge.networking.AdornNetworking
import juuxel.adorn.platform.forge.networking.BrewerFluidSyncS2CMessage
import juuxel.adorn.platform.forge.networking.OpenBookS2CMessage
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraftforge.network.NetworkHooks
import net.minecraftforge.network.PacketDistributor

object NetworkBridgeImpl : NetworkBridge {
    override fun sendToTracking(entity: Entity, packet: Packet<*>) =
        PacketDistributor.TRACKING_ENTITY.with { entity }.send(packet)

    @Suppress("UNCHECKED_CAST") // forge pls
    override fun createEntitySpawnPacket(entity: Entity): Packet<ClientPlayPacketListener> =
        NetworkHooks.getEntitySpawningPacket(entity) as Packet<ClientPlayPacketListener>

    override fun sendOpenBookPacket(player: PlayerEntity, bookId: Identifier) {
        if (player is ServerPlayerEntity) {
            AdornNetworking.CHANNEL.send(PacketDistributor.PLAYER.with { player }, OpenBookS2CMessage(bookId))
        }
    }

    override fun sendBrewerFluidSync(player: PlayerEntity, syncId: Int, fluid: FluidReference) {
        if (player is ServerPlayerEntity) {
            AdornNetworking.CHANNEL.send(PacketDistributor.PLAYER.with { player }, BrewerFluidSyncS2CMessage(syncId, fluid.createSnapshot()))
        }
    }
}
