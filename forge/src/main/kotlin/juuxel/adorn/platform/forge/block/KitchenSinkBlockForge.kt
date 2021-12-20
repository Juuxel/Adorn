package juuxel.adorn.platform.forge.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.KitchenSinkBlock
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class KitchenSinkBlockForge(variant: BlockVariant) : KitchenSinkBlock(variant), BlockEntityProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.KITCHEN_SINK.instantiate(pos, state)
}
