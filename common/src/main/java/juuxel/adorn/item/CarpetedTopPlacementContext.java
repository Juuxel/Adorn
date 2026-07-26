package juuxel.adorn.item;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public final class CarpetedTopPlacementContext extends BlockPlaceContext {
    public CarpetedTopPlacementContext(UseOnContext context) {
        super(context);
        // We know that the block is a carpet block
        replaceClicked = true;
    }
}
