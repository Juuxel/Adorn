package juuxel.adorn.block.entity

import juuxel.adorn.util.getText
import juuxel.adorn.util.putText
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class TradingStationBlockEntityForge(pos: BlockPos, state: BlockState) : TradingStationBlockEntity(pos, state) {
    override fun toInitialChunkDataNbt(): NbtCompound = writeNbt(NbtCompound())

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
        val nbt = NbtCompound()
        nbt.putText(NBT_TRADING_OWNER_NAME, ownerName)
        nbt.put(NBT_TRADE, trade.writeNbt(NbtCompound()))
        return BlockEntityUpdateS2CPacket(pos, -1, nbt)
    }

    // overrides IForgeBlockEntity.onDataPacket
    fun onDataPacket(net: ClientConnection, packet: BlockEntityUpdateS2CPacket) {
        val tag = packet.nbt
        trade.readNbt(tag.getCompound(NBT_TRADE))
        ownerName = tag.getText(NBT_TRADING_OWNER_NAME) ?: return
    }
}
