package juuxel.adorn.lib

import io.netty.buffer.Unpooled
import juuxel.adorn.Adorn
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.trading.Trade
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.network.packet.CustomPayloadS2CPacket
import net.minecraft.client.network.packet.EntitySpawnS2CPacket
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos

object ModNetworking {
    val ENTITY_SPAWN = Adorn.id("entity_spawn")
    val TRADE_SYNC = Adorn.id("trade_sync")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ClientSidePacketRegistry.INSTANCE.register(ENTITY_SPAWN) { context, buf ->
            val packet = EntitySpawnS2CPacket()
            packet.read(buf)
            context.taskQueue.execute {
                val world = context.player?.world ?: return@execute
                val entity = packet.entityTypeId.create(world)!!
                entity.entityId = packet.id
                entity.uuid = packet.uuid
                entity.updateTrackedPosition(packet.x, packet.y, packet.z)
                entity.pitch = packet.pitch * 360 / 256f
                entity.yaw = packet.yaw * 360 / 256f

                (context.player.world as? ClientWorld)?.addEntity(packet.id, entity)
            }
        }

        ClientSidePacketRegistry.INSTANCE.register(TRADE_SYNC) { context, buf ->
            val pos = buf.readBlockPos()
            val be = context.player?.world?.getBlockEntity(pos) as? TradingStationBlockEntity ?: return@register
            be.trade.fromTag(buf.readCompoundTag()!!)
        }
    }

    fun createEntitySpawnPacket(entity: Entity) =
        CustomPayloadS2CPacket(ENTITY_SPAWN, PacketByteBuf(Unpooled.buffer()).apply {
            EntitySpawnS2CPacket(entity).write(this)
        })

    fun createTradeSyncPacket(pos: BlockPos, trade: Trade) =
        CustomPayloadS2CPacket(TRADE_SYNC, PacketByteBuf(Unpooled.buffer()).apply {
            writeBlockPos(pos)
            writeCompoundTag(trade.toTag(CompoundTag()))
        })
}
