package juuxel.adorn.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

data class BlockEntityDescriptor<B : Block, E : BlockEntity>(val type: Class<B>, val factory: (BlockPos, BlockState) -> E) {
    companion object {
        inline operator fun <reified B : Block, E : BlockEntity> invoke(noinline factory: (BlockPos, BlockState) -> E) =
            BlockEntityDescriptor(B::class.java, factory)
    }
}
