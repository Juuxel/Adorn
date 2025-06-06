package juuxel.adorn.item;

import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;

import java.util.Optional;

public final class TradingStationItem extends BlockItem {
    public TradingStationItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean canBeNested() {
        // Don't allow putting trading stations inside shulker boxes or other trading stations.
        return false;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        var trade = stack.get(AdornComponentTypes.TRADE.get());
        if (trade != null && !trade.isFullyEmpty()) {
            return Optional.of(trade);
        }

        return Optional.empty();
    }
}
