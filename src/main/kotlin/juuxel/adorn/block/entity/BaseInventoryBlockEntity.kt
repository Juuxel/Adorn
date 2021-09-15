package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.SimpleSidedInventory
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    private val invSize: Int
) : LootableContainerBlockEntity(type), ExtendedScreenHandlerFactory {
    private val _containerName by lazy {
        // For EP names
        ItemStack(cachedState.block).name
    }
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
    val sidedInventory: SidedInventory = @Suppress("LeakingThis") SimpleSidedInventory(this)

    override fun writeNbt(tag: NbtCompound) = super.writeNbt(tag).apply {
        if (!serializeLootTable(tag))
            Inventories.writeNbt(tag, items)
    }

    override fun fromTag(state: BlockState, tag: NbtCompound) {
        super.fromTag(state, tag)
        if (!deserializeLootTable(tag))
            Inventories.readNbt(tag, items)
    }

    override fun isEmpty() = InventoryComponent.hasContents(items)

    override fun getInvStackList() = items

    override fun setInvStackList(items: DefaultedList<ItemStack>) {
        this.items = items
    }

    override fun size() = invSize

    override fun getContainerName() = _containerName

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }

    // InventoryProvider implementation for blocks

    interface InventoryProviderImpl : InventoryProvider {
        override fun getInventory(state: BlockState?, world: WorldAccess, pos: BlockPos): SidedInventory? =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.sidedInventory
    }
}
