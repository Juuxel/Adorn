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
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

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
    private static final Style BOOK_CONTENTS_STYLE = Style.EMPTY.withoutShadow().withColor(Colors.SCREEN_TEXT);

    private final Book book;
    private FlipBook flipBook;
    private PageButton previousPageButton;
    private PageButton nextPageButton;
    private final AnimationEngine animationEngine = new AnimationEngine();

    public GuideBookScreen(Book book) {
        super(CommonComponents.EMPTY);
        this.book = book;
    }

    @Override
    protected void init() {
        int x = (width - BOOK_SIZE) / 2;
        int y = (height - BOOK_SIZE) / 2;
        int pageX = x + 35;
        int pageY = y + 14;

        addRenderableWidget(new CloseButton(x + 142, y + 14, button -> onClose()));
        previousPageButton = addRenderableWidget(new PageButton(x + 49, y + 159, false, widget -> flipBook.showPreviousPage(), true));
        nextPageButton = addRenderableWidget(new PageButton(x + 116, y + 159, true, widget -> flipBook.showNextPage(), true));

        // The flip book has to be added last so that
        // its mouse hover tooltip renders on top of all widgets.
        flipBook = addRenderableWidget(new FlipBook(this::updatePageTurnButtons));
        flipBook.add(new TitlePage(pageX, pageY));
        for (var page : book.pages()) {
            var body = new BookPageBody(pageX, pageY + PAGE_TEXT_Y, page);
            var panel = new BookPagePanel(body);
            panel.add(new BookPageTitle(pageX, pageY, page));
            panel.add(new ScrollEnvelope(pageX, pageY + PAGE_TEXT_Y, PAGE_WIDTH, PAGE_BODY_HEIGHT, body, animationEngine, false));
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
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractBackground(context, mouseX, mouseY, delta);
        int x = (width - BOOK_SIZE) / 2;
        int y = (height - BOOK_SIZE) / 2;
        context.blit(RenderPipelines.GUI_TEXTURED, BookViewScreen.BOOK_LOCATION, x, y, 0, 0, BOOK_SIZE, BOOK_SIZE, 256, 256);
    }

    private boolean handleTextClick(@Nullable ClickEvent clickEvent) {
        if (clickEvent instanceof ClickEvent.ChangePage(int page)) {
            var pageIndex = page - 1; // 1-indexed => 0-indexed

            if (0 <= pageIndex && pageIndex < flipBook.getPageCount()) {
                flipBook.setCurrentPage(pageIndex);
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1f));
                return true;
            }
        }

        return false;
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
    public boolean keyPressed(KeyEvent input) {
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

    private final class TitlePage implements GuiEventListener, Renderable {
        private final int x;
        private final int y;
        private final Component byAuthor = Component.translatable("book.byAuthor", book.author());
        private boolean focused = false;

        private TitlePage(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            int cx = x + PAGE_WIDTH / 2;
            var matrices = context.pose();
            matrices.pushMatrix();
            matrices.translate(cx, y + 7 + 25);
            matrices.scale(book.titleScale(), book.titleScale());
            context.text(font, book.title(), -font.width(book.title()) / 2, 0, Colors.SCREEN_TEXT, false);
            matrices.popMatrix();

            context.text(font, book.subtitle(), cx - font.width(book.subtitle()) / 2, y + 45, Colors.SCREEN_TEXT, false);
            context.text(font, byAuthor, cx - font.width(byAuthor) / 2, y + 60, Colors.SCREEN_TEXT, false);
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

    private final class BookPagePanel extends Panel {
        private final BookPageBody body;

        private BookPagePanel(BookPageBody body) {
            this.body = body;
        }
    }

    private final class BookPageTitle implements GuiEventListener, Renderable, TickingElement {
        private final int x;
        private final int y;
        private final List<FormattedCharSequence> wrappedTitleLines;
        private final List<ItemStack> icons;
        private int icon = 0;
        private int iconTicks = 0;
        private boolean focused = false;

        private BookPageTitle(int x, int y, Page page) {
            this.x = x;
            this.y = y;
            this.wrappedTitleLines = font.split(page.title().copy().withStyle(style -> style.withBold(true)), PAGE_TITLE_WIDTH);
            this.icons = CollectionUtil.interleave(page.icons().stream().map(Page.Icon::createStacks).toList());
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            context.fakeItem(icons.get(icon), x, y);

            int titleY = y + 10 - font.lineHeight * wrappedTitleLines.size() / 2;
            for (int i = 0; i < wrappedTitleLines.size(); i++) {
                var line = wrappedTitleLines.get(i);
                context.text(font, line, x + PAGE_TITLE_X, titleY + i * font.lineHeight, Colors.SCREEN_TEXT, false);
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

    private final class BookPageBody implements SizedElement, Renderable {
        private final int x;
        private final int y;
        private final Page page;
        private final List<FormattedCharSequence> wrappedBodyLines;
        private final int textHeight;
        private final int imageHeight;
        private final int height;
        private boolean focused;

        private BookPageBody(int x, int y, Page page) {
            this.x = x;
            this.y = y;
            this.page = page;
            this.wrappedBodyLines = font.split(ComponentUtils.mergeStyles(page.text(), BOOK_CONTENTS_STYLE), PAGE_WIDTH - PAGE_TEXT_X);
            this.textHeight = wrappedBodyLines.size() * font.lineHeight;
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

        @Override
        public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            drawText(context.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR));

            if (page.image() != null) {
                renderImage(context, page.image(), mouseX, mouseY);
            }
        }

        private void drawText(ActiveTextCollector drawer) {
            int textYOffset = page.image() != null && page.image().placement() == Image.Placement.BEFORE_TEXT ? imageHeight : 0;

            for (int i = 0; i < wrappedBodyLines.size(); i++) {
                var line = wrappedBodyLines.get(i);
                drawer.accept(x + PAGE_TEXT_X, textYOffset + y + i * font.lineHeight, line);
            }
        }

        private void renderImage(GuiGraphicsExtractor context, Image image, int mouseX, int mouseY) {
            var imageX = x + (PAGE_WIDTH - image.size().x()) / 2;
            var imageY = switch (image.placement()) {
                case BEFORE_TEXT -> y;
                case AFTER_TEXT -> y + textHeight + PAGE_IMAGE_GAP;
            };

            context.blit(RenderPipelines.GUI_TEXTURED, image.location(), imageX, imageY, 0f, 0f, image.size().x(), image.size().y(), image.size().x(), image.size().y());

            for (var hoverArea : image.hoverAreas()) {
                if (hoverArea.contains(mouseX - imageX, mouseY - imageY)) {
                    var hX = imageX + hoverArea.position().x();
                    var hY = imageY + hoverArea.position().y();
                    context.fill(hX, hY, hX + hoverArea.size().x(), hY + hoverArea.size().y(), HOVER_AREA_HIGHLIGHT_COLOR);

                    var wrappedTooltip = font.split(hoverArea.tooltip(), PAGE_WIDTH);
                    Scissors.suspendScissors(context, () -> context.setTooltipForNextFrame(font, wrappedTooltip, mouseX, mouseY));
                    break;
                }
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
            if (click.button() == 0) {
                if (flipBook.getCurrentPageValue() instanceof BookPagePanel panel) {
                    var clickHandler = new ActiveTextCollector.ClickableStyleFinder(font, (int) click.x(), (int) click.y());
                    panel.body.drawText(clickHandler);
                    var style = clickHandler.result();
                    if (style != null && handleTextClick(style.getClickEvent())) {
                        return true;
                    }
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

    private final class CloseButton extends Button {
        private CloseButton(int x, int y, OnPress onPress) {
            super(x, y, 8, 8, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
            var texture = isHovered() ? CLOSE_BOOK_ACTIVE_TEXTURE : CLOSE_BOOK_INACTIVE_TEXTURE;
            context.blit(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), 0f, 0f, 8, 8, 8, 8);
        }
    }
}
