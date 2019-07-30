package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.SidedInventoryImpl
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.TranslatableText
import net.minecraft.util.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    private val invSize: Int
) : LootableContainerBlockEntity(type) {
    private val _containerName by lazy {
        // For EP names
        ItemStack(cachedState.block).name
    }
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
    val sidedInventory: SidedInventory = @Suppress("LeakingThis") SidedInventoryImpl(this)

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        if (!serializeLootTable(tag))
            Inventories.toTag(tag, items)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        if (!deserializeLootTable(tag))
            Inventories.fromTag(tag, items)
    }

    override fun isInvEmpty() = InventoryComponent.isInvEmpty(items)

    override fun getInvStackList() = items

    override fun setInvStackList(items: DefaultedList<ItemStack>) {
        this.items = items
    }

    override fun getInvSize() = invSize

    override fun getContainerName() = _containerName

    // InventoryProvider implementation for blocks

    interface InventoryProviderImpl : InventoryProvider {
        override fun getInventory(state: BlockState?, world: IWorld, pos: BlockPos): SidedInventory? =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.sidedInventory
    }
}
