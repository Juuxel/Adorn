package juuxel.adorn.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import juuxel.adorn.util.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;

public record HeaderFooterOptions(int headerHeight, int footerHeight) {
    public static final int DEFAULT_HEADER_FOOTER_HEIGHT = 33;

    public HeaderFooterOptions(int headerFooterHeight) {
        this(headerFooterHeight, headerFooterHeight);
    }

    public HeaderFooterOptions() {
        this(DEFAULT_HEADER_FOOTER_HEIGHT);
    }

    public int backgroundY() {
        return headerHeight;
    }

    public int backgroundHeight(int screenHeight) {
        return screenHeight - headerHeight - footerHeight;
    }

    public int footerWidgetYFromBottom(int widgetHeight) {
        return (footerHeight + widgetHeight) / 2;
    }

    public void renderTitle(DrawContext context, Text title, int screenWidth) {
        var client = MinecraftClient.getInstance();
        context.drawCenteredTextWithShadow(client.textRenderer, title, screenWidth / 2, (headerHeight - client.textRenderer.fontHeight) / 2, Colors.WHITE);
    }

    public void renderBackground(DrawContext context, int screenWidth, int screenHeight) {
        RenderSystem.enableBlend();

        var client = MinecraftClient.getInstance();

        // Draw darkened background
        int backgroundHeight = backgroundHeight(screenHeight);
        var background = client.world == null ? EntryListWidget.MENU_LIST_BACKGROUND_TEXTURE : EntryListWidget.INWORLD_MENU_LIST_BACKGROUND_TEXTURE;
        context.drawTexture(background, 0, backgroundY(), screenWidth, headerHeight + backgroundHeight, screenWidth, backgroundHeight, 32, 32);

        // Draw headers and footers
        var headerSeparator = client.world == null ? Screen.HEADER_SEPARATOR_TEXTURE : Screen.INWORLD_HEADER_SEPARATOR_TEXTURE;
        var footerSeparator = client.world == null ? Screen.FOOTER_SEPARATOR_TEXTURE : Screen.INWORLD_FOOTER_SEPARATOR_TEXTURE;
        context.drawTexture(headerSeparator, 0, backgroundY() - 2, 0, 0, screenWidth, 2, 32, 2);
        context.drawTexture(footerSeparator, 0, screenHeight - footerHeight, 0, 0, screenWidth, 2, 32, 2);

        RenderSystem.disableBlend();
    }
}
