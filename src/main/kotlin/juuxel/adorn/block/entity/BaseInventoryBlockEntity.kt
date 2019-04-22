package juuxel.adorn.block.entity

/*import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import alexiil.mc.lib.attributes.item.impl.SimpleFixedItemInv
import io.github.juuxel.polyester.registry.PolyesterBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.ContainerProvider
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BaseInventoryBlockEntity(
    type: BlockEntityType<*>,
    invSize: Int
) : BlockEntity(type), ContainerProvider, AttributeProvider {
    val inventory = SimpleFixedItemInv(invSize)

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        inventory.toTag(tag)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        inventory.fromTag(tag)
    }

    override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
        attributes.offer(inventory)
        attributes.offer(inventory.extractable)
        attributes.offer(inventory.insertable)
        attributes.offer(inventory.statistics)
    }

    // Uses PolyesterBlock to force being applied to a block
    interface BlockAttributeProviderImpl : PolyesterBlock, AttributeProvider {
        override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.let {
                it.addAllAttributes(world, pos, state, attributes)
            }
        }
    }
}*/
