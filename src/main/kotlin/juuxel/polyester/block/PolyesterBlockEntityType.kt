package juuxel.polyester.block

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

class PolyesterBlockEntityType<T : BlockEntity> private constructor(
    supplier: () -> T,
    private val blocks: MutableSet<Block>
) : BlockEntityType<T>(supplier, blocks, null) {
    constructor(supplier: () -> T) : this(supplier, HashSet())

    internal fun addBlock(block: Block) {
        blocks += block
    }
}
