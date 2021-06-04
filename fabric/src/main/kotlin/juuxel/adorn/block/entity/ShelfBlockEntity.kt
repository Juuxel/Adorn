package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class ShelfBlockEntity(pos: BlockPos, state: BlockState) :
    BaseInventoryBlockEntity(AdornBlockEntities.SHELF, pos, state, 2), BlockEntityClientSerializable {
    // No menus for shelves
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory?) = null

    override fun getMaxCountPerStack() = 1

    override fun toClientTag(nbt: NbtCompound) = nbt.apply {
        Inventories.writeNbt(nbt, items)
    }

    override fun fromClientTag(nbt: NbtCompound) {
        Inventories.readNbt(nbt, items)
    }
}
