package juuxel.adorn.lib

import io.netty.buffer.Unpooled
import juuxel.adorn.Adorn
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.mixin.gamerule.GameRulesAccessor
import juuxel.adorn.mixin.gamerule.RuleAccessor
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.JavaUtils
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
import net.minecraft.world.GameRules
import org.apache.logging.log4j.LogManager

object AdornNetworking {
    private val LOGGER = LogManager.getLogger("Adorn|Networking")

    val ENTITY_SPAWN = Adorn.id("entity_spawn")
    val GAME_RULE = Adorn.id("game_rule")

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

        ClientSidePacketRegistry.INSTANCE.register(GAME_RULE) { context, buf ->
            val requested = buf.readString()
            val value = buf.readString()

            context.taskQueue.execute {
                val key = GameRulesAccessor.getRules().keys.find { it.getName() == requested }
                if (key == null) {
                    LOGGER.warn("[Adorn] Received unknown game rule packet: $requested")
                    return@execute
                }
                @Suppress("UNCHECKED_CAST")
                context.player.world?.let { world ->
                    // I hate this cast
                    val rule: GameRules.Rule<*> = world.gameRules.get(key as GameRules.RuleKey<GameRules.BooleanRule>)
                    (rule as RuleAccessor).callSetFromString(value)
                }
            }
        }
    }

    fun createEntitySpawnPacket(entity: Entity) =
        CustomPayloadS2CPacket(ENTITY_SPAWN, PacketByteBuf(Unpooled.buffer()).apply {
            EntitySpawnS2CPacket(entity).write(this)
        })
}
