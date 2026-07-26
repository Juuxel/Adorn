package juuxel.adorn.item;

import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Optional;

public final class TradingStationItem extends BlockItem {
    public TradingStationItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        // Don't allow putting trading stations inside shulker boxes or other trading stations.
        return false;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var trade = stack.get(AdornComponentTypes.TRADE.get());
        if (trade != null && !trade.isFullyEmpty()) {
            return Optional.of(trade);
        }

        return Optional.empty();
    }
}
