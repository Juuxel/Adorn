package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
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
    invSize: Int
) : BlockEntity(type) {
    val items: InventoryComponent = InventoryComponent(invSize)
    val sidedInventory: SidedInventory = items.sidedInventory

    init {
        items.addListener {
            markDirty()
        }
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        items.toTag(tag)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        items.fromTag(tag)
    }

    // InventoryProvider implementation for blocks

    interface InventoryProviderImpl : InventoryProvider {
        override fun getInventory(state: BlockState?, world: IWorld, pos: BlockPos): SidedInventory? =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.sidedInventory
    }
}
