package juuxel.adorn.block.entity

import juuxel.adorn.util.InventoryComponent
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.Container
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    invSize: Int
) : BlockEntity(type), NameableContainerProvider {
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

    override fun getDisplayName() = TranslatableComponent(cachedState.block.translationKey)

    // InventoryProvider implementation for blocks

    interface InventoryProviderImpl : InventoryProvider {
        override fun getInventory(state: BlockState?, world: IWorld, pos: BlockPos): SidedInventory? =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.sidedInventory
    }
}
