package juuxel.adorn.util

import alexiil.mc.lib.attributes.Simulation
import alexiil.mc.lib.attributes.item.FixedItemInv
import alexiil.mc.lib.attributes.item.impl.PartialInventoryFixedWrapper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class SidedFixedInventoryWrapper(private val inv: FixedItemInv) : PartialInventoryFixedWrapper(inv), SidedInventory {
    override fun canPlayerUseInv(player: PlayerEntity?) = true

    override fun getInvAvailableSlots(var1: Direction?) = IntArray(inv.slotCount) { it }

    override fun canExtractInvStack(slot: Int, stack: ItemStack, direction: Direction?) =
        !inv.transferable.attemptExtraction(
            { ItemStack.areEqualIgnoreTags(it, stack) },
            stack.amount,
            Simulation.SIMULATE
        ).isEmpty

    override fun canInsertInvStack(slot: Int, stack: ItemStack, direction: Direction?) =
        inv.transferable.attemptInsertion(stack, Simulation.SIMULATE).isEmpty
}
