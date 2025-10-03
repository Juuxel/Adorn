package juuxel.adorn.client.renderer;

import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public final class TradingStationRenderState extends BlockEntityRenderState {
    public final TradeRenderState trade = new TradeRenderState();
    public Text ownerName;
    public float rotationInTicks;
    public float tickProgress;

    public static final class TradeRenderState {
        public ItemRenderState selling;
        public Text sellingLabel;
        public Text priceLabel;
        public boolean empty;

        public void setFrom(Trade trade, ItemModelManager itemModelManager, World world, int seed) {
            empty = trade.isEmpty();
            selling = new ItemRenderState();
            itemModelManager.clearAndUpdate(selling, trade.getSelling(), ItemDisplayContext.FIXED, world, null, seed);
            sellingLabel = AdornUtil.toTextWithCount(trade.getSelling());
            priceLabel = AdornUtil.toTextWithCount(trade.getPrice());
        }
    }
}
