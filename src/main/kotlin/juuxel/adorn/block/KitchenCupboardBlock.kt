package juuxel.adorn.block

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class KitchenCupboardBlock(material: String) : BaseKitchenCounterBlock(), BlockEntityProvider, AttributeProvider {
    override val name = "${material}_kitchen_cupboard"
    //override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun createBlockEntity(view: BlockView?) = BLOCK_ENTITY_TYPE.instantiate()

    override fun addAllAttributes(world: World, pos: BlockPos, state: BlockState, attributes: AttributeList<*>) {
        (world.getBlockEntity(pos) as? KitchenCupboardBlockEntity)?.let {
            attributes.offer(it.inventory)
            attributes.offer(it.inventory.extractable)
            attributes.offer(it.inventory.insertable)
            attributes.offer(it.inventory.statistics)
        }
    }

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<KitchenCupboardBlockEntity> =
            BlockEntityType(::KitchenCupboardBlockEntity, null)
    }
}
