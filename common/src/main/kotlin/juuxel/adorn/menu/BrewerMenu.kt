package juuxel.adorn.menu

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.item.AdornItems
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.menu.Slot
import net.minecraft.menu.property.ArrayPropertyDelegate
import net.minecraft.menu.property.PropertyDelegate
import kotlin.math.min

class BrewerMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val container: Inventory,
    private val propertyDelegate: PropertyDelegate,
    var fluid: FluidReference
) : Menu(AdornMenus.BREWER, syncId) {
    val progress: Int get() = propertyDelegate[0]
    private val player: PlayerEntity = playerInventory.player
    private var lastFluid: FluidVolume? = null

    constructor(syncId: Int, playerInventory: PlayerInventory) :
        this(syncId, playerInventory, SimpleInventory(BrewerBlockEntity.CONTAINER_SIZE), ArrayPropertyDelegate(1), FluidVolume.empty(FluidUnit.LITRE))

    init {
        checkSize(container, BrewerBlockEntity.CONTAINER_SIZE)
        checkDataCount(propertyDelegate, 1)

        addSlot(MainSlot(container, BrewerBlockEntity.INPUT_SLOT, 80, 56))
        addSlot(Slot(container, BrewerBlockEntity.LEFT_INGREDIENT_SLOT, 50, 17))
        addSlot(Slot(container, BrewerBlockEntity.RIGHT_INGREDIENT_SLOT, 110, 17))
        addSlot(FluidContainerSlot(container, BrewerBlockEntity.FLUID_CONTAINER_SLOT, 123, 60))

        // Main player inventory
        for (y in 0..2) {
            for (x in 0..8) {
                addSlot(Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18))
            }
        }

        // Hotbar
        for (x in 0..8) {
            addSlot(Slot(playerInventory, x, 8 + x * 18, 142))
        }

        addProperties(propertyDelegate)
    }

    override fun canUse(player: PlayerEntity): Boolean = container.canPlayerUse(player)

    override fun transferSlot(player: PlayerEntity, index: Int): ItemStack {
        var result = ItemStack.EMPTY
        val slot = slots[index]

        if (slot.hasStack()) {
            val stack = slot.stack
            result = stack.copy()

            if (index <= BrewerBlockEntity.FLUID_CONTAINER_SLOT) {
                if (!insertItem(stack, 3, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else {
                val mugSlot = slots[BrewerBlockEntity.INPUT_SLOT]

                if (!mugSlot.hasStack() && mugSlot.canInsert(stack)) {
                    mugSlot.stack = stack.split(min(mugSlot.getMaxItemCount(stack), stack.count))
                }

                if (!stack.isEmpty && !insertItem(stack, BrewerBlockEntity.LEFT_INGREDIENT_SLOT, BrewerBlockEntity.FLUID_CONTAINER_SLOT + 1, false)) {
                    return ItemStack.EMPTY
                }
            }

            if (stack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return result
    }

    override fun sendContentUpdates() {
        super.sendContentUpdates()

        val last = lastFluid
        if (last == null || !FluidReference.areFluidsAndAmountsEqual(fluid, last)) {
            lastFluid = fluid.createSnapshot()
            PlatformBridges.network.sendBrewerFluidSync(player, syncId, fluid)
        }
    }

    private class MainSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun getMaxItemCount(): Int = 1

        override fun canInsert(stack: ItemStack): Boolean =
            stack.isOf(AdornItems.MUG)
    }

    private class FluidContainerSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun getMaxItemCount(): Int = 1
    }
}
