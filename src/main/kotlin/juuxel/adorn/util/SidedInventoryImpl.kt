package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class SidedInventoryImpl(private val inv: Inventory) : Inventory by inv, SidedInventory {
    private val slots: IntArray = IntArray(inv.invSize) { it }

    override fun canPlayerUseInv(player: PlayerEntity?) = true

    override fun getInvAvailableSlots(var1: Direction?) = slots

    override fun canExtractInvStack(slot: Int, stack: ItemStack, direction: Direction?) = true

    override fun canInsertInvStack(slot: Int, stack: ItemStack, direction: Direction?) = true
}
