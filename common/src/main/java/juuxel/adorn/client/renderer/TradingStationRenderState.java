package juuxel.adorn.client.renderer;

import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public final class TradingStationRenderState extends BlockEntityRenderState {
    public final TradeRenderState trade = new TradeRenderState();
    public Component ownerName;
    public float rotationInTicks;
    public float tickProgress;

    public static final class TradeRenderState {
        public ItemStackRenderState selling;
        public Component sellingLabel;
        public Component priceLabel;
        public boolean empty;

        public void setFrom(Trade trade, ItemModelResolver itemModelManager, Level world, int seed) {
            empty = trade.isEmpty();
            selling = new ItemStackRenderState();
            itemModelManager.updateForTopItem(selling, trade.getSelling(), ItemDisplayContext.FIXED, world, null, seed);
            sellingLabel = AdornUtil.toTextWithCount(trade.getSelling());
            priceLabel = AdornUtil.toTextWithCount(trade.getPrice());
        }
    }
}
