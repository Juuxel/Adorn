package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.ClientNetworkBridge;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.networking.SetTradeStackC2SMessage;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Logging;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public final class TradingStationScreen extends AdornMenuScreen<TradingStationMenu> {
    private static final Logger LOGGER = Logging.logger();
    private static final Identifier BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/trading_station.png");
    private static final Text SELLING_LABEL = Text.translatable("block.adorn.trading_station.selling");
    private static final Text PRICE_LABEL = Text.translatable("block.adorn.trading_station.price");

    public TradingStationScreen(TradingStationMenu menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
        backgroundHeight = 186;
        playerInventoryTitleY = backgroundHeight - 94; // copied from MenuScreen.<init>
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, title, titleX, titleY, Colors.WHITE, false);
        context.drawText(textRenderer, playerInventoryTitle, playerInventoryTitleX, playerInventoryTitleY, Colors.WHITE, false);
        context.drawText(textRenderer, SELLING_LABEL, 26 + 9 - textRenderer.getWidth(SELLING_LABEL) / 2, 25, Colors.WHITE, false);
        context.drawText(textRenderer, PRICE_LABEL, 26 + 9 - textRenderer.getWidth(PRICE_LABEL) / 2, 61, Colors.WHITE, false);
    }

    /**
     * Updates the trade selling/price stack in the specified slot.
     * This function is mostly meant for item viewer drag-and-drop interactions.
     */
    public void updateTradeStack(Slot slot, ItemStack stack) {
        if (!TradingStationMenu.isValidItem(stack)) {
            LOGGER.error("Trying to set invalid item {} for slot {} in trading station", stack, slot);
            return;
        }

        slot.setStack(stack);
        ClientNetworkBridge.get().sendToServer(new SetTradeStackC2SMessage(menu.syncId, slot.id, stack));
    }
}
