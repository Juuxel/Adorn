package juuxel.adorn.block.entity

import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.world.BlockView

/**
 * An interface for blocks that provide their block entity type.
 */
interface BETypeProvider : BlockEntityProvider {
    val blockEntityType: BlockEntityType<*>

    override fun createBlockEntity(view: BlockView?) = blockEntityType.instantiate()
}
