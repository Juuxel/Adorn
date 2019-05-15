package juuxel.adorn.item

import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.hit.BlockHitResult

/**
 * An `ItemPlacementContext` subclass for placing blocks inside carpets.
 */
class CarpetedTopPlacementContext(parent: ItemUsageContext) : ItemPlacementContext(
    parent.world,
    parent.player,
    parent.hand,
    parent.itemStack,
    BlockHitResult(parent.pos, parent.facing, parent.blockPos, parent.method_17699())
) {
    init {
        // We know that the block is a carpet block
        canReplaceHitBlock = true
    }
}
