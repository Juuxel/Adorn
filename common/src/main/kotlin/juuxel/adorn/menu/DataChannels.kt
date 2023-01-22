package juuxel.adorn.menu

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap

class DataChannels {
    private val channels: Byte2ObjectMap<DataChannel<*>> = Byte2ObjectOpenHashMap()

    fun get(id: Byte): DataChannel<*>? =
        channels[id]

    fun <T : Any> register(channel: DataChannel<T>): DataChannel<T> {
        if (channels.put(channel.id, channel) != null) {
            throw IllegalArgumentException("Channel ID ${channel.id} already registered!")
        }

        return channel
    }

    fun tick() {
        for (channel in channels.values) {
            channel.tick()
        }
    }
}
