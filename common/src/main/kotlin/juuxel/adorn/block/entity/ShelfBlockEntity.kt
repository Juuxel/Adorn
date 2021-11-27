package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

abstract class ShelfBlockEntity(pos: BlockPos, state: BlockState) :
    BaseContainerBlockEntity(AdornBlockEntities.SHELF, pos, state, 2) {
    // No menus for shelves
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory?) = null

    override fun getMaxCountPerStack() = 1

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound =
        createNbt()
}
