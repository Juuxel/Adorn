package juuxel.adorn.block.entity

import juuxel.adorn.util.SidedInventoryImpl
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    private val invSize: Int
) : BlockEntity(type), Inventory {
    val items: DefaultedList<ItemStack> = DefaultedList.create(invSize, ItemStack.EMPTY)
    val sidedInventory: SidedInventory = SidedInventoryImpl(this)

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        Inventories.toTag(tag, items)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        Inventories.fromTag(tag, items)
    }

    // Inventory implementation

    override fun getInvStack(slot: Int) = items[slot]

    override fun clear() {
        items.clear()
        markDirty()
    }

    override fun setInvStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    override fun removeInvStack(slot: Int) =
        Inventories.removeStack(items, slot)

    override fun canPlayerUseInv(player: PlayerEntity?) = true

    override fun getInvSize() = invSize

    override fun takeInvStack(p0: Int, p1: Int) =
        Inventories.splitStack(items, p0, p1).also {
            if (!it.isEmpty) {
                markDirty()
            }
        }

    override fun isInvEmpty(): Boolean {
        for (stack in items) {
            if (!stack.isEmpty) {
                return false
            }
        }

        return true
    }

    // InventoryProvider implementation for blocks

    interface InventoryProviderImpl : InventoryProvider {
        override fun getInventory(state: BlockState?, world: IWorld, pos: BlockPos): SidedInventory? =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.let {
                it.sidedInventory
            }
    }
}
