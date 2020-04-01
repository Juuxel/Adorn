package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class SidedInventoryImpl(private val inv: Inventory) : Inventory by inv, SidedInventory {
    private val slots: IntArray = IntArray(inv.size()) { it }

    override fun canPlayerUse(player: PlayerEntity?) = true

    override fun getAvailableSlots(var1: Direction?) = slots

    override fun canExtract(slot: Int, stack: ItemStack, direction: Direction?) = true

    override fun canInsert(slot: Int, stack: ItemStack, direction: Direction?) = true
}
