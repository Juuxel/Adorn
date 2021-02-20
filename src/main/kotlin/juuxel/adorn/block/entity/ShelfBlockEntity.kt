package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket

class ShelfBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.SHELF.get(), 2) {
    // No menus for shelves
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory?) = null

    override fun getMaxCountPerStack() = 1

    override fun toInitialChunkDataTag() = toTag(CompoundTag())

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
        val tag = CompoundTag()
        Inventories.toTag(tag, items)
        return BlockEntityUpdateS2CPacket(pos, -1, tag)
    }

    override fun onDataPacket(net: ClientConnection, packet: BlockEntityUpdateS2CPacket) {
        val tag = packet.compoundTag
        Inventories.fromTag(tag, items)
    }
}
