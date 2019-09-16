package juuxel.adorn.block.entity

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class MutableBlockEntityType<T : BlockEntity> private constructor(
    supplier: () -> T,
    private val blocks: MutableSet<Block>
) : BlockEntityType<T>(supplier, blocks, null) {
    constructor(supplier: () -> T) : this(supplier, HashSet())

    fun addBlock(block: Block) {
        blocks += block
    }
}
