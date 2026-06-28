package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.gui.widget.ConfigScreenHeading;
import juuxel.adorn.client.gui.widget.ConfigScreenLabel;
import juuxel.adorn.client.gui.widget.Draggable;
import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Displayable;
import juuxel.adorn.util.PropertyRef;
import juuxel.adorn.util.animation.AnimationEngine;
import juuxel.adorn.util.animation.AnimationTask;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractConfigScreen extends Screen {
    private static final int HEADER_FOOTER_HEIGHT = 33;
    private static final int CONFIG_BUTTON_START_Y = HEADER_FOOTER_HEIGHT + 2;
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_GAP = 4;
    public static final int BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_GAP;
    protected static final int BACK_BUTTON_Y_FROM_BOTTOM = (HEADER_FOOTER_HEIGHT + BUTTON_HEIGHT) / 2;
    private static final int HEART_SIZE = 12;
    private static final int[] HEART_COLORS = new int[] {
        0xFF_FF0000, // Red
        0xFF_FC8702, // Orange
        0xFF_FFFF00, // Yellow
        0xFF_A7FC58, // Green
        0xFF_2D61FC, // Blue
        0xFF_A002FC, // Purple
        0xFF_58E9FC, // Light blue
        0xFF_FCA1DF, // Pink
    };
    private static final Identifier HEART_TEXTURE = AdornCommon.id("textures/gui/heart.png");
    private static final float MIN_HEART_SPEED = 0.05f;
    private static final float MAX_HEART_SPEED = 1.5f;
    private static final float MAX_HEART_ANGULAR_SPEED = 0.07f;
    private static final int HEART_CHANCE = 65;

    private final Screen parent;

    private final Layout layout;
    private final int backgroundY = HEADER_FOOTER_HEIGHT;
    private int backgroundHeight;
    private Panel mainPanel;

    private final Random random = new Random();
    private final List<Heart> hearts = new ArrayList<>();
    private boolean restartRequired = false;
    private final AnimationEngine animationEngine = new AnimationEngine();

    /** The Y-coordinate of the next config option or heading to be added. */
    protected int nextChildY = CONFIG_BUTTON_START_Y;

    protected AbstractConfigScreen(Text title, Screen parent, Layout layout) {
        super(title);
        this.parent = parent;
        this.layout = layout;
        animationEngine.add(new HeartAnimationTask());
    }

    @Override
    protected void init() {
        nextChildY = CONFIG_BUTTON_START_Y;
        animationEngine.start();

        backgroundHeight = height - backgroundY - HEADER_FOOTER_HEIGHT;
        mainPanel = new Panel();
        initConfigWidgets(mainPanel);
        var sizedPanel = mainPanel.asSized(layout.totalWidth(), nextChildY - backgroundY - BUTTON_GAP);
        addDrawableChild(
            new ScrollEnvelope(
                computeLeftMarginX(),
                backgroundY,
                layout.totalWidth() + ScrollEnvelope.ADDED_WIDTH + 2,
                backgroundHeight,
                sizedPanel,
                animationEngine,
                true
            )
        );
    }

    protected abstract void initConfigWidgets(Panel panel);

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, (HEADER_FOOTER_HEIGHT - textRenderer.fontHeight) / 2, Colors.WHITE);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        super.renderBackground(context, mouseX, mouseY, tickProgress);

        // Draw hearts
        synchronized (hearts) {
            renderHearts(context, tickProgress);
        }

        // Draw darkened background
        var background = client.world == null ? EntryListWidget.MENU_LIST_BACKGROUND_TEXTURE : EntryListWidget.INWORLD_MENU_LIST_BACKGROUND_TEXTURE;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, background, 0, backgroundY, width, backgroundY + backgroundHeight, width, backgroundHeight, 32, 32);

        // Draw headers and footers
        var headerSeparator = client.world == null ? Screen.HEADER_SEPARATOR_TEXTURE : Screen.INWORLD_HEADER_SEPARATOR_TEXTURE;
        var footerSeparator = client.world == null ? Screen.FOOTER_SEPARATOR_TEXTURE : Screen.INWORLD_FOOTER_SEPARATOR_TEXTURE;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, headerSeparator, 0, backgroundY - 2, 0, 0, width, 2, 32, 2);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, footerSeparator, 0, height - HEADER_FOOTER_HEIGHT, 0, 0, width, 2, 32, 2);
    }

    private void renderHearts(DrawContext context, float delta) {
        for (var heart : hearts) {
            var matrices = context.getMatrices();
            matrices.pushMatrix();
            matrices.translate(heart.x, MathHelper.lerp(delta, heart.previousY, heart.y));
            matrices.translate(0.5f * HEART_SIZE, 0.5f * HEART_SIZE);
            var angle = MathHelper.lerp(delta, heart.previousAngle, heart.angle);
            matrices.rotate(angle);
            matrices.translate(-0.5f * HEART_SIZE, -0.5f * HEART_SIZE);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, HEART_TEXTURE, 0, 0, 0f, 0f, HEART_SIZE, HEART_SIZE, 8, 8, 8, 8, heart.color);
            matrices.popMatrix();
        }
    }

    @Override
    public void close() {
        client.setScreen(restartRequired ? new NoticeScreen(
            () -> client.setScreen(parent),
            Text.translatable("gui.adorn.config.restart_required.title"),
            Text.translatable("gui.adorn.config.restart_required.message"),
            Text.translatable("gui.ok"),
            true
        ) : parent);
    }

    @Override
    public void removed() {
        animationEngine.stop();
    }

    private void tickHearts() {
        var iter = hearts.iterator();
        while (iter.hasNext()) {
            var heart = iter.next();

            if (heart.y - HEART_SIZE > height) {
                iter.remove();
            } else {
                heart.move();
            }
        }

        if (random.nextInt(HEART_CHANCE) == 0) {
            int x = random.nextInt(width);
            int color = HEART_COLORS[random.nextInt(HEART_COLORS.length)];
            float speed = random.nextFloat(MIN_HEART_SPEED, MAX_HEART_SPEED);
            float angularSpeed = random.nextFloat(-MAX_HEART_ANGULAR_SPEED, MAX_HEART_ANGULAR_SPEED);
            hearts.add(new Heart(x, -HEART_SIZE, color, speed, angularSpeed));
        }
    }

    private Tooltip createTooltip(PropertyRef<?> property, boolean restartRequired) {
        var text = Text.translatable(getTooltipTranslationKey(property.getName()));
        if (restartRequired) {
            text.append(Text.literal("\n"))
                .append(Text.translatable("gui.adorn.config.requires_restart").formatted(Formatting.ITALIC, Formatting.GOLD));
        }
        return Tooltip.of(text);
    }

    private <T> CyclingButtonWidget<T> createConfigButton(CyclingButtonWidget.Builder<T> builder, PropertyRef<T> property, boolean restartRequired) {
        int x = computeButtonX();
        int y = nextChildY;

        if (layout instanceof Layout.Tabular) {
            builder.omitKeyText();
        }

        return builder
            .tooltip(value -> createTooltip(property, restartRequired))
            .build(x, y, layout.buttonWidth(), BUTTON_HEIGHT, Text.translatable(getOptionTranslationKey(property.getName())), (button, value) -> {
                property.set(value);
                ConfigManager.get().save();

                if (restartRequired) {
                    this.restartRequired = true;
                }
            });
    }

    private int computeLeftMarginX() {
        return (width - layout.totalWidth()) / 2;
    }

    private int computeButtonX() {
        return switch (layout) {
            case Layout.BigButtons(int totalWidth) -> computeLeftMarginX();
            case Layout.Tabular(int totalWidth, int buttonWidth) -> (width + totalWidth) / 2 - buttonWidth;
        };
    }

    private void addConfigLabelIfNeeded(PropertyRef<?> property, boolean restartRequired) {
        if (layout instanceof Layout.Tabular(int totalWidth, int buttonWidth)) {
            var tooltipState = new TooltipState();
            tooltipState.setTooltip(createTooltip(property, restartRequired));
            var label = new ConfigScreenLabel(
                Text.translatable(getOptionTranslationKey(property.getName())),
                tooltipState,
                computeLeftMarginX(),
                nextChildY,
                totalWidth - buttonWidth
            );
            mainPanel.addStandaloneDrawable(label);
        }
    }

    protected void addConfigToggle(PropertyRef<Boolean> property) {
        addConfigToggle(property, false);
    }

    protected void addConfigToggle(PropertyRef<Boolean> property, boolean restartRequired) {
        var button = createConfigButton(
            CyclingButtonWidget.onOffBuilder(property.get()),
            property, restartRequired
        );

        addConfigLabelIfNeeded(property, restartRequired);
        mainPanel.add(button);
        nextChildY += BUTTON_SPACING;
    }

    protected <T extends Displayable> void addConfigButton(PropertyRef<T> property, List<T> values) {
        addConfigButton(property, values, false);
    }

    protected <T extends Displayable> void addConfigButton(PropertyRef<T> property, List<T> values, boolean restartRequired) {
        var button = createConfigButton(
            CyclingButtonWidget.<T>builder(Displayable::getDisplayName, property.get()).values(values),
            property, restartRequired
        );

        addConfigLabelIfNeeded(property, restartRequired);
        mainPanel.add(button);
        nextChildY += BUTTON_SPACING;
    }

    protected void addHeading(Text text) {
        mainPanel.addStandaloneDrawable(new ConfigScreenHeading(text, (width - layout.totalWidth()) / 2, nextChildY, layout.totalWidth()));
        nextChildY += ConfigScreenHeading.HEIGHT;
    }

    protected String getOptionTranslationKey(String name) {
        return "gui.adorn.config.option." + name;
    }

    private String getTooltipTranslationKey(String name) {
        return getOptionTranslationKey(name) + ".description";
    }

    @Override
    public void tick() {
        if (getFocused() instanceof Draggable focused && !isDragging()) {
            focused.stopDragging();
        }
    }

    private static final class Heart {
        private final int x;
        private float y;
        private final int color;
        private final float speed;
        private final float angularSpeed;
        private float previousY;
        private float previousAngle = 0f;
        private float angle = 0f;

        private Heart(int x, float y, int color, float speed, float angularSpeed) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.speed = speed;
            this.angularSpeed = angularSpeed;
            previousY = y;
        }

        private void move() {
            previousY = y;
            y += speed;
            previousAngle = angle;
            angle = (angle + angularSpeed) % MathHelper.TAU;
        }
    }

    private final class HeartAnimationTask implements AnimationTask {
        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void tick() {
            synchronized (hearts) {
                tickHearts();
            }
        }
    }

    public sealed interface Layout {
        int totalWidth();
        int buttonWidth();

        record BigButtons(int buttonWidth) implements Layout {
            @Override
            public int totalWidth() {
                return buttonWidth;
            }
        }

        record Tabular(int totalWidth, int buttonWidth) implements Layout {
        }
    }
}
