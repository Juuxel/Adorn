package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class SimpleSidedInventory(private val parent: Inventory) : Inventory by parent, SidedInventory {
    private val slots: IntArray = IntArray(parent.size()) { it }

    override fun canPlayerUse(player: PlayerEntity?) = true

    override fun getAvailableSlots(side: Direction?) = slots

    override fun canExtract(slot: Int, stack: ItemStack, side: Direction?) = true

    override fun canInsert(slot: Int, stack: ItemStack, side: Direction?) = true
}
