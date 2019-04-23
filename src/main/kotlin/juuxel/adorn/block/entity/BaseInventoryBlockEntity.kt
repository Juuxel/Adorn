package juuxel.adorn.block.entity

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import alexiil.mc.lib.attributes.item.impl.SimpleFixedItemInv
import io.github.juuxel.polyester.registry.PolyesterBlock
import juuxel.adorn.util.SidedFixedInventoryWrapper
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    invSize: Int
) : BlockEntity(type), AttributeProvider/*, InventoryProvider*/ {
    val inventory = SimpleFixedItemInv(invSize)

    init {
        inventory.addListener({ _, _, _, _ -> markDirty() }, {})
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        put("Items", inventory.toTag())
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        inventory.fromTag(tag.getCompound("Items"))
    }

    override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
        attributes.offer(inventory)
        attributes.offer(inventory.extractable)
        attributes.offer(inventory.insertable)
    }

//    override fun getInventory(state: BlockState, world: IWorld, pos: BlockPos) =
//        SidedFixedInventoryWrapper(inventory)

    // Uses PolyesterBlock to force being applied to a block
    interface BlockAttributeProviderImpl : PolyesterBlock, AttributeProvider, InventoryProvider {
        override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)
                ?.addAllAttributes(world, pos, state, attributes)
        }

        override fun getInventory(state: BlockState, world: IWorld, pos: BlockPos) =
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.let {
                SidedFixedInventoryWrapper(it.inventory)
            }
    }
}
