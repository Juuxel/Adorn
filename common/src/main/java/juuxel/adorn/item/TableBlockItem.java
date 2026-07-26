package juuxel.adorn.item;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;

public final class TableBlockItem extends BlockItem {
    public TableBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var world = context.getLevel();
        var pos = context.getClickedPos();
        if (context.getClickedFace() == Direction.UP && world.getBlockState(pos).getBlock() instanceof CarpetBlock) {
            return place(new CarpetedTopPlacementContext(context));
        }

        return super.useOn(context);
    }
}
