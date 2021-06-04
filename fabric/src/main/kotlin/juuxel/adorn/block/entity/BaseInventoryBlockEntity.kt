package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
    private val invSize: Int
) : LootableContainerBlockEntity(type, pos, state), ExtendedScreenHandlerFactory {
    protected var items: DefaultedList<ItemStack> = DefaultedList.ofSize(invSize, ItemStack.EMPTY)

    override fun writeNbt(nbt: NbtCompound) = super.writeNbt(nbt).apply {
        if (!serializeLootTable(nbt))
            Inventories.writeNbt(nbt, items)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        if (!deserializeLootTable(nbt))
            Inventories.readNbt(nbt, items)
    }

    override fun isEmpty() = InventoryComponent.hasContents(items)

    override fun getInvStackList() = items

    override fun setInvStackList(items: DefaultedList<ItemStack>) {
        this.items = items
    }

    override fun size() = invSize

    override fun getContainerName(): Text = cachedState.block.name

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }
}
