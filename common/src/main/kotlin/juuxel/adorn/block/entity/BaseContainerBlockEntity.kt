package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

/**
 * A container block entity that might not have a menu.
 * This class handles the serialisation and the container logic.
 */
abstract class BaseContainerBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState, size: Int) :
    LootableContainerBlockEntity(type, pos, state) {
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(size, ItemStack.EMPTY)

    override fun writeNbt(nbt: NbtCompound) = super.writeNbt(nbt).apply {
        if (!serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, items)
        }
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (!deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, items)
        }
    }

    override fun isEmpty() = InventoryComponent.hasContents(items)

    override fun getInvStackList() = items

    override fun setInvStackList(items: DefaultedList<ItemStack>) {
        this.items = items
    }

    override fun size() = items.size

    override fun getContainerName(): Text = cachedState.block.name
}
