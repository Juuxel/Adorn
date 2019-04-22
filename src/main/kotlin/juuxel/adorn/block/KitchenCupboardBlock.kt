package juuxel.adorn.block

import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.world.BlockView

class KitchenCupboardBlock(
    material: String
) : BaseKitchenCounterBlock(), BlockEntityProvider, BaseInventoryBlockEntity.BlockAttributeProviderImpl {
    override val name = "${material}_kitchen_cupboard"
    //override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun createBlockEntity(view: BlockView?) = BLOCK_ENTITY_TYPE.instantiate()

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<KitchenCupboardBlockEntity> =
            BlockEntityType(::KitchenCupboardBlockEntity, null)
    }
}
