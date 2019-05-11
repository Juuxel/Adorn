package juuxel.adorn.block.entity

import juuxel.adorn.patcher.patches.Lba2AdornInventoryPatch
import juuxel.adorn.util.InventoryComponent
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.NameableContainerProvider
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    invSize: Int
) : PatchableBlockEntity(type), NameableContainerProvider {
    val items: InventoryComponent = InventoryComponent(invSize)
    val sidedInventory: SidedInventory = items.sidedInventory
    override val patches = super.patches + Lba2AdornInventoryPatch()

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
