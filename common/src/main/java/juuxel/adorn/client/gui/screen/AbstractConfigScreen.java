package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.gui.widget.ConfigScreenHeading;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Displayable;
import juuxel.adorn.util.PropertyRef;
import juuxel.adorn.util.animation.AnimationEngine;
import juuxel.adorn.util.animation.AnimationTask;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractConfigScreen extends Screen {
    private static final int CONFIG_BUTTON_START_Y = 40;
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_GAP = 4;
    public static final int BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_GAP;
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
    private static final double MIN_HEART_SPEED = 0.05;
    private static final double MAX_HEART_SPEED = 1.5;
    private static final double MAX_HEART_ANGULAR_SPEED = 0.07;
    private static final int HEART_CHANCE = 65;

    private final Screen parent;
    private final Random random = new Random();
    private final List<Heart> hearts = new ArrayList<>();
    private boolean restartRequired = false;
    private final AnimationEngine animationEngine = new AnimationEngine();

    /** The Y-coordinate of the next config option or heading to be added. */
    protected int nextChildY = CONFIG_BUTTON_START_Y;

    protected AbstractConfigScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
        animationEngine.add(new HeartAnimationTask());
    }

    @Override
    protected void init() {
        nextChildY = CONFIG_BUTTON_START_Y;
        animationEngine.start();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, Colors.WHITE);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float tickProgress) {
        super.renderBackground(context, mouseX, mouseY, tickProgress);
        synchronized (hearts) {
            renderHearts(context, tickProgress);
        }
    }

    private void renderHearts(DrawContext context, float delta) {
        for (var heart : hearts) {
            var matrices = context.getMatrices();
            matrices.push();
            matrices.translate(heart.x, MathHelper.lerp(delta, heart.previousY, heart.y), 0.0);
            matrices.translate(0.5 * HEART_SIZE, 0.5 * HEART_SIZE, 0.0);
            var angle = MathHelper.lerp(delta, heart.previousAngle, heart.angle);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) angle));
            matrices.translate(-0.5 * HEART_SIZE, -0.5 * HEART_SIZE, 0.0);
            context.drawTexture(RenderLayer::getGuiTextured, HEART_TEXTURE, 0, 0, 0f, 0f, HEART_SIZE, HEART_SIZE, 8, 8, 8, 8, heart.color);
            matrices.pop();
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
            double speed = random.nextDouble(MIN_HEART_SPEED, MAX_HEART_SPEED);
            double angularSpeed = random.nextDouble(-MAX_HEART_ANGULAR_SPEED, MAX_HEART_ANGULAR_SPEED);
            hearts.add(new Heart(x, -HEART_SIZE, color, speed, angularSpeed));
        }
    }

    private <T> CyclingButtonWidget<T> createConfigButton(CyclingButtonWidget.Builder<T> builder, int x, int y, int width, PropertyRef<T> property, boolean restartRequired) {
        return builder
            .tooltip(value -> {
                var text = Text.translatable(getTooltipTranslationKey(property.getName()));
                if (restartRequired) {
                    text.append(Text.literal("\n"))
                        .append(Text.translatable("gui.adorn.config.requires_restart").formatted(Formatting.ITALIC, Formatting.GOLD));
                }
                return Tooltip.of(text);
            })
            .build(x, y, width, BUTTON_HEIGHT, Text.translatable(getOptionTranslationKey(property.getName())), (button, value) -> {
                property.set(value);
                ConfigManager.get().save();

                if (restartRequired) {
                    this.restartRequired = true;
                }
            });
    }

    protected void addConfigToggle(int width, PropertyRef<Boolean> property) {
        addConfigToggle(width, property, false);
    }

    protected void addConfigToggle(int width, PropertyRef<Boolean> property, boolean restartRequired) {
        var button = createConfigButton(
            CyclingButtonWidget.onOffBuilder(property.get()),
            (this.width - width) / 2, nextChildY, width, property, restartRequired
        );
        addDrawableChild(button);
        nextChildY += BUTTON_SPACING;
    }

    protected <T extends Displayable> void addConfigButton(int width, PropertyRef<T> property, List<T> values) {
        addConfigButton(width, property, values, false);
    }

    protected <T extends Displayable> void addConfigButton(int width, PropertyRef<T> property, List<T> values, boolean restartRequired) {
        var button = createConfigButton(
            CyclingButtonWidget.<T>builder(Displayable::getDisplayName).values(values).initially(property.get()),
            (this.width - width) / 2, nextChildY, width, property, restartRequired
        );
        addDrawableChild(button);
        nextChildY += BUTTON_SPACING;
    }

    protected void addHeading(Text text, int width) {
        addDrawable(new ConfigScreenHeading(text, (this.width - width) / 2, nextChildY, width));
        nextChildY += ConfigScreenHeading.HEIGHT;
    }

    protected String getOptionTranslationKey(String name) {
        return "gui.adorn.config.option." + name;
    }

    private String getTooltipTranslationKey(String name) {
        return getOptionTranslationKey(name) + ".description";
    }

    private static final class Heart {
        private final int x;
        private double y;
        private final int color;
        private final double speed;
        private final double angularSpeed;
        private double previousY;
        private double previousAngle = 0.0;
        private double angle = 0.0;

        private Heart(int x, double y, int color, double speed, double angularSpeed) {
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
}
