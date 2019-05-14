package juuxel.adorn.item

import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.hit.BlockHitResult

class CarpetloggableTopPlacementContext(parent: ItemUsageContext) : ItemPlacementContext(
    parent.world,
    parent.player,
    parent.hand,
    parent.itemStack,
    BlockHitResult(parent.pos.subtract(0.0, 1.0, 0.0), parent.facing, parent.blockPos.down(), parent.method_17699())
)
