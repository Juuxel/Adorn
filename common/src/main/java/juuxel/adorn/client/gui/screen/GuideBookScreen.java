package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.book.Book;
import juuxel.adorn.client.book.Image;
import juuxel.adorn.client.book.Page;
import juuxel.adorn.client.gui.Scissors;
import juuxel.adorn.client.gui.widget.Draggable;
import juuxel.adorn.client.gui.widget.FlipBook;
import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import juuxel.adorn.client.gui.widget.SizedElement;
import juuxel.adorn.client.gui.widget.TickingElement;
import juuxel.adorn.util.CollectionUtil;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.animation.AnimationEngine;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class GuideBookScreen extends Screen {
    private static final int BOOK_SIZE = 192;
    private static final int PAGE_TITLE_X = 20;
    private static final int PAGE_WIDTH = 116;
    private static final int PAGE_BODY_HEIGHT = 121;
    private static final int PAGE_TITLE_WIDTH = PAGE_WIDTH - 2 * PAGE_TITLE_X;
    private static final int PAGE_TEXT_X = 4;
    private static final int PAGE_TEXT_Y = 24;
    private static final int PAGE_IMAGE_GAP = 4;
    private static final int ICON_DURATION = 25;
    private static final Identifier CLOSE_BOOK_ACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_active.png");
    private static final Identifier CLOSE_BOOK_INACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_inactive.png");
    private static final int HOVER_AREA_HIGHLIGHT_COLOR = 0x80_FFFFFF;

    private final Book book;
    private FlipBook flipBook;
    private PageTurnWidget previousPageButton;
    private PageTurnWidget nextPageButton;
    private final AnimationEngine animationEngine = new AnimationEngine();

    public GuideBookScreen(Book book) {
        super(ScreenTexts.EMPTY);
        this.book = book;
    }

    @Override
    protected void init() {
        int x = (width - BOOK_SIZE) / 2;
        int y = (height - BOOK_SIZE) / 2;
        int pageX = x + 35;
        int pageY = y + 14;

        addDrawableChild(new CloseButton(x + 142, y + 14, button -> close()));
        previousPageButton = addDrawableChild(new PageTurnWidget(x + 49, y + 159, false, widget -> flipBook.showPreviousPage(), true));
        nextPageButton = addDrawableChild(new PageTurnWidget(x + 116, y + 159, true, widget -> flipBook.showNextPage(), true));

        // The flip book has to be added last so that
        // its mouse hover tooltip renders on top of all widgets.
        flipBook = addDrawableChild(new FlipBook(this::updatePageTurnButtons));
        flipBook.add(new TitlePage(pageX, pageY));
        for (var page : book.pages()) {
            var panel = new Panel();
            panel.add(new BookPageTitle(pageX, pageY, page));
            var body = new BookPageBody(pageX, pageY + PAGE_TEXT_Y, page);
            panel.add(new ScrollEnvelope(pageX, pageY + PAGE_TEXT_Y, PAGE_WIDTH, PAGE_BODY_HEIGHT, body, animationEngine));
            flipBook.add(panel);
        }

        updatePageTurnButtons();
        animationEngine.start();
    }

    private void updatePageTurnButtons() {
        previousPageButton.visible = flipBook.hasPreviousPage();
        nextPageButton.visible = flipBook.hasNextPage();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        int x = (width - BOOK_SIZE) / 2;
        int y = (height - BOOK_SIZE) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BookScreen.BOOK_TEXTURE, x, y, 0, 0, BOOK_SIZE, BOOK_SIZE, 256, 256);
    }

    @Override
    public boolean handleTextClick(@Nullable Style style) {
        if (style != null) {
            var clickEvent = style.getClickEvent();

            if (clickEvent instanceof ClickEvent.ChangePage(int page)) {
                var pageIndex = page - 1; // 1-indexed => 0-indexed

                if (0 <= pageIndex && pageIndex < flipBook.getPageCount()) {
                    flipBook.setCurrentPage(pageIndex);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f));
                    return true;
                }
            }
        }

        return super.handleTextClick(style);
    }

    @Override
    public void tick() {
        for (var child : children()) {
            if (child instanceof TickingElement ticking) ticking.tick();
        }

        if (getFocused() instanceof Draggable focused && !isDragging()) {
            focused.stopDragging();
        }
    }

    @Override
    public void removed() {
        animationEngine.stop();
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (super.keyPressed(input)) {
            return true;
        }

        if (flipBook.getCurrentPageValue() instanceof Panel currentPage) {
            for (var child : currentPage.children()) {
                if (child instanceof ScrollEnvelope) {
                    return child.keyPressed(input);
                }
            }
        }

        return false;
    }

    private final class TitlePage implements Element, Drawable {
        private final int x;
        private final int y;
        private final Text byAuthor = Text.translatable("book.byAuthor", book.author());
        private boolean focused = false;

        private TitlePage(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            int cx = x + PAGE_WIDTH / 2;
            var matrices = context.getMatrices();
            matrices.pushMatrix();
            matrices.translate(cx, y + 7 + 25);
            matrices.scale(book.titleScale(), book.titleScale());
            context.drawText(textRenderer, book.title(), -textRenderer.getWidth(book.title()) / 2, 0, Colors.SCREEN_TEXT, false);
            matrices.popMatrix();

            context.drawText(textRenderer, book.subtitle(), cx - textRenderer.getWidth(book.subtitle()) / 2, y + 45, Colors.SCREEN_TEXT, false);
            context.drawText(textRenderer, byAuthor, cx - textRenderer.getWidth(byAuthor) / 2, y + 60, Colors.SCREEN_TEXT, false);
        }

        @Override
        public boolean isFocused() {
            return focused;
        }

        @Override
        public void setFocused(boolean focused) {
            this.focused = focused;
        }
    }

    private final class BookPageTitle implements Element, Drawable, TickingElement {
        private final int x;
        private final int y;
        private final List<OrderedText> wrappedTitleLines;
        private final List<ItemStack> icons;
        private int icon = 0;
        private int iconTicks = 0;
        private boolean focused = false;

        private BookPageTitle(int x, int y, Page page) {
            this.x = x;
            this.y = y;
            this.wrappedTitleLines = textRenderer.wrapLines(page.title().copy().styled(style -> style.withBold(true)), PAGE_TITLE_WIDTH);
            this.icons = CollectionUtil.interleave(page.icons().stream().map(Page.Icon::createStacks).toList());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawItemWithoutEntity(icons.get(icon), x, y);

            int titleY = y + 10 - textRenderer.fontHeight * wrappedTitleLines.size() / 2;
            for (int i = 0; i < wrappedTitleLines.size(); i++) {
                var line = wrappedTitleLines.get(i);
                context.drawText(textRenderer, line, x + PAGE_TITLE_X, titleY + i * textRenderer.fontHeight, Colors.SCREEN_TEXT, false);
            }
        }

        @Override
        public void tick() {
            if (iconTicks++ >= ICON_DURATION) {
                iconTicks = 0;
                icon = (icon + 1) % icons.size();
            }
        }

        @Override
        public boolean isFocused() {
            return focused;
        }

        @Override
        public void setFocused(boolean focused) {
            this.focused = focused;
        }
    }

    private final class BookPageBody implements SizedElement, Drawable {
        private final int x;
        private final int y;
        private final Page page;
        private final List<OrderedText> wrappedBodyLines;
        private final int textHeight;
        private final int imageHeight;
        private final int height;
        private boolean focused;

        private BookPageBody(int x, int y, Page page) {
            this.x = x;
            this.y = y;
            this.page = page;
            this.wrappedBodyLines = textRenderer.wrapLines(page.text(), PAGE_WIDTH - PAGE_TEXT_X);
            this.textHeight = wrappedBodyLines.size() * textRenderer.fontHeight;
            this.imageHeight = page.image() != null ? page.image().size().y() + PAGE_IMAGE_GAP : 0;
            this.height = Math.max(PAGE_BODY_HEIGHT, textHeight + imageHeight);
        }

        @Override
        public int getWidth() {
            return PAGE_WIDTH;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height;
        }

        private @Nullable Style getTextStyleAt(int x, int y) {
            if (!isMouseOver(x, y)) return null;

            // coordinates in widget-space
            int wx = x - (this.x + PAGE_TEXT_X);
            int wy = y - this.y;
            int lineIndex = wy / textRenderer.fontHeight;

            if (0 <= lineIndex && lineIndex < wrappedBodyLines.size()) {
                var line = wrappedBodyLines.get(lineIndex);
                return textRenderer.getTextHandler().getStyleAt(line, wx);
            }

            return null;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            int textYOffset = page.image() != null && page.image().placement() == Image.Placement.BEFORE_TEXT ? imageHeight : 0;

            for (int i = 0; i < wrappedBodyLines.size(); i++) {
                var line = wrappedBodyLines.get(i);
                context.drawText(textRenderer, line, x + PAGE_TEXT_X, textYOffset + y + i * textRenderer.fontHeight, Colors.SCREEN_TEXT, false);
            }

            if (page.image() != null) {
                renderImage(context, page.image(), mouseX, mouseY);
            }

            var hoveredStyle = getTextStyleAt(mouseX, mouseY);
            Scissors.suspendScissors(context, () -> context.drawHoverEvent(textRenderer, hoveredStyle, mouseX, mouseY));
        }

        private void renderImage(DrawContext context, Image image, int mouseX, int mouseY) {
            var imageX = x + (PAGE_WIDTH - image.size().x()) / 2;
            var imageY = switch (image.placement()) {
                case BEFORE_TEXT -> y;
                case AFTER_TEXT -> y + textHeight + PAGE_IMAGE_GAP;
            };

            context.drawTexture(RenderPipelines.GUI_TEXTURED, image.location(), imageX, imageY, 0f, 0f, image.size().x(), image.size().y(), image.size().x(), image.size().y());

            for (var hoverArea : image.hoverAreas()) {
                if (hoverArea.contains(mouseX - imageX, mouseY - imageY)) {
                    var hX = imageX + hoverArea.position().x();
                    var hY = imageY + hoverArea.position().y();
                    context.fill(hX, hY, hX + hoverArea.size().x(), hY + hoverArea.size().y(), HOVER_AREA_HIGHLIGHT_COLOR);

                    var wrappedTooltip = textRenderer.wrapLines(hoverArea.tooltip(), PAGE_WIDTH);
                    Scissors.suspendScissors(context, () -> context.drawOrderedTooltip(textRenderer, wrappedTooltip, mouseX, mouseY));
                    break;
                }
            }
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            if (click.button() == 0) {
                var style = getTextStyleAt((int) click.x(), (int) click.y());

                if (style != null && handleTextClick(style)) {
                    return true;
                }
            }

            return SizedElement.super.mouseClicked(click, doubled);
        }

        @Override
        public boolean isFocused() {
            return focused;
        }

        @Override
        public void setFocused(boolean focused) {
            this.focused = focused;
        }
    }

    private final class CloseButton extends ButtonWidget {
        private CloseButton(int x, int y, PressAction onPress) {
            super(x, y, 8, 8, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            var texture = isHovered() ? CLOSE_BOOK_ACTIVE_TEXTURE : CLOSE_BOOK_INACTIVE_TEXTURE;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), 0f, 0f, 8, 8, 8, 8);
        }
    }
}
