package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.NbtCompound

class ShelfBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.SHELF, 2), BlockEntityClientSerializable {
    // No menus for shelves
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory?) = null

    override fun getMaxCountPerStack() = 1

    override fun toClientTag(tag: NbtCompound) = tag.apply {
        Inventories.writeNbt(tag, items)
    }

    override fun fromClientTag(tag: NbtCompound) {
        Inventories.readNbt(tag, items)
    }
}
