package juuxel.adorn.item;

import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.TallBlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public final class ChairBlockItem extends TallBlockItem {
    public ChairBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        if (context.getSide() == Direction.UP && world.getBlockState(pos).getBlock() instanceof CarpetBlock) {
            return place(new CarpetedTopPlacementContext(context));
        }

        return super.useOnBlock(context);
    }
}
