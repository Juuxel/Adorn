package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.ShelfBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class ShelfBlockEntityForge(pos: BlockPos, state: BlockState) : ShelfBlockEntity(pos, state) {
    override fun toInitialChunkDataNbt(): NbtCompound = writeNbt(NbtCompound())

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
        val nbt = NbtCompound()
        Inventories.writeNbt(nbt, items)
        return BlockEntityUpdateS2CPacket(pos, -1, nbt)
    }

    override fun onDataPacket(net: ClientConnection, packet: BlockEntityUpdateS2CPacket) {
        val nbt = packet.nbt
        Inventories.readNbt(nbt, items)
    }
}
