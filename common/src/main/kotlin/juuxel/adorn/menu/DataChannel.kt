package juuxel.adorn.menu

import juuxel.adorn.client.ClientNetworkBridge
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf

class DataChannel<T : Any>(
    private val player: PlayerEntity,
    val menuSyncId: Int,
    val id: Byte,
    private val getter: () -> T,
    private val setter: (T) -> Unit,
    private val copier: (T) -> T,
    private val reader: (PacketByteBuf) -> T,
    private val writer: (PacketByteBuf, T) -> Unit,
) {
    private val isClient: Boolean = player.world.isClient
    private var cached: T? = null

    fun read(buf: PacketByteBuf) {
        val value = reader(buf)
        setter(value)
        cached = value
    }

    fun write(buf: PacketByteBuf) {
        val value = if (isClient) {
            // No cached values on the client
            getter()
        } else {
            cached ?: throw IllegalStateException("[Adorn] Writing data channel outside of tick!")
        }

        writer(buf, value)
    }

    fun tick() {
        if (isClient) return

        val current = getter()
        if (current != cached) {
            cached = copier(current)
            PlatformBridges.network.sendDataChannelToClient(player, this)
        }
    }

    fun pushToServer() {
        if (!isClient) {
            throw IllegalStateException("[Adorn] pushToServer can only be called on the logical client!")
        }

        ClientNetworkBridge.get().sendDataChannelToServer(this)
    }
}
