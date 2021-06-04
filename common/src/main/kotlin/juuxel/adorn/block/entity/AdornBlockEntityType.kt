package juuxel.adorn.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class AdornBlockEntityType<E : BlockEntity>(
    factory: (BlockPos, BlockState) -> E,
    private val blockPredicate: (Block) -> Boolean
) : BlockEntityType<E>(factory, emptySet(), null) {
    override fun supports(state: BlockState) = blockPredicate(state.block)
}
