package juuxel.adorn.block.entity

import net.minecraft.block.entity.BlockEntity

data class BlockEntityDescriptor<B : BlockEntity>(
    val type: Class<B>,
    val factory: () -> B
) {
    companion object {
        inline operator fun <reified B : BlockEntity> invoke(noinline factory: () -> B) =
            BlockEntityDescriptor(B::class.java, factory)
    }
}
