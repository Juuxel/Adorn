package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.ClientNetworkBridge;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.networking.SetTradeStackC2SMessage;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Logging;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public final class TradingStationScreen extends AdornMenuScreen<TradingStationMenu> {
    private static final Logger LOGGER = Logging.logger();
    private static final Identifier BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/trading_station.png");
    private static final Component SELLING_LABEL = Component.translatable("block.adorn.trading_station.selling");
    private static final Component PRICE_LABEL = Component.translatable("block.adorn.trading_station.price");

    public TradingStationScreen(TradingStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DEFAULT_IMAGE_WIDTH, 186);
        inventoryLabelY = imageHeight - 94; // copied from MenuScreen.<init>
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractBackground(context, mouseX, mouseY, delta);
        context.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        context.text(font, title, titleLabelX, titleLabelY, Colors.WHITE, false);
        context.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, Colors.WHITE, false);
        context.text(font, SELLING_LABEL, 26 + 9 - font.width(SELLING_LABEL) / 2, 25, Colors.WHITE, false);
        context.text(font, PRICE_LABEL, 26 + 9 - font.width(PRICE_LABEL) / 2, 61, Colors.WHITE, false);
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

        slot.setByPlayer(stack);
        ClientNetworkBridge.get().sendToServer(new SetTradeStackC2SMessage(menu.containerId, slot.index, stack));
    }
}
