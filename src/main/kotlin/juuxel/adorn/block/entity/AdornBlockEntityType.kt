package juuxel.adorn.block.entity

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class AdornBlockEntityType<E : BlockEntity>(
    factory: () -> E,
    private val blockPredicate: (Block) -> Boolean
) : BlockEntityType<E>(factory, emptySet(), null) {
    override fun supports(block: Block) = blockPredicate(block)
}
