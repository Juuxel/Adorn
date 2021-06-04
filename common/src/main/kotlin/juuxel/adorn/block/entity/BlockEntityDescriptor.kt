package juuxel.adorn.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

data class BlockEntityDescriptor<B : Block>(
    val type: Class<B>,
    val factory: (BlockPos, BlockState) -> BlockEntity
) {
    companion object {
        inline operator fun <reified B : Block> invoke(noinline factory: (BlockPos, BlockState) -> BlockEntity) =
            BlockEntityDescriptor(B::class.java, factory)
    }
}
