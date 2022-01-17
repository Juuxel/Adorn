package juuxel.adorn.compat

import com.google.common.collect.ListMultimap
import juuxel.adorn.lib.Registered
import net.minecraft.block.Block

interface ClientCompatLoader {
    fun init(blocks: ListMultimap<BlockKind, Registered<Block>>)
}
