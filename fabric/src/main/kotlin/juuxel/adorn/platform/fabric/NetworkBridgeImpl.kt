package juuxel.adorn.platform.fabric

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.lib.AdornNetworking
import juuxel.adorn.platform.NetworkBridge
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.util.Identifier

object NetworkBridgeImpl : NetworkBridge {
    override fun sendToTracking(entity: Entity, packet: Packet<*>) {
        for (player in PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.getSender(player).sendPacket(packet)
        }
    }

    override fun createEntitySpawnPacket(entity: Entity): Packet<ClientPlayPacketListener> =
        AdornNetworking.createEntitySpawnPacket(entity)

    override fun sendOpenBookPacket(player: PlayerEntity, bookId: Identifier) =
        AdornNetworking.sendOpenBookPacket(player, bookId)

    override fun sendBrewerFluidSync(player: PlayerEntity, syncId: Int, fluid: FluidReference) {
        AdornNetworking.sendBrewerFluidSync(player, syncId, fluid)
    }
}
