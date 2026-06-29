package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.HeaderFooterOptions;
import juuxel.adorn.client.gui.widget.MultilineTextElement;
import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.animation.AnimationEngine;
import juuxel.adorn.util.verification.CompatCheckMode;
import juuxel.adorn.util.verification.EnumVerifier;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class ModCompatWarningScreen extends Screen {
    private static final int CONTENT_WIDTH = 250;
    private static final int VERTICAL_PADDING = 2;
    private final HeaderFooterOptions headerFooterOptions = new HeaderFooterOptions();
    private final Screen previous;
    private final boolean fromConfigScreen;
    private final AnimationEngine animationEngine = new AnimationEngine();

    private ModCompatWarningScreen(Screen previous, boolean fromConfigScreen) {
        super(Text.translatable("gui.adorn.mod_compatibility_warning"));
        this.previous = previous;
        this.fromConfigScreen = fromConfigScreen;
    }

    public static Screen checkAndCreate(Screen previous, boolean fromConfigScreen) {
        EnumVerifier.verifyEnums();

        if (fromConfigScreen || !EnumVerifier.getViolations().isEmpty()) {
            return new ModCompatWarningScreen(previous, fromConfigScreen);
        }

        return previous;
    }

    @Override
    protected void init() {
        super.init();
        animationEngine.start();

        var violations = EnumVerifier.getViolations();
        boolean noIssues = violations.isEmpty();
        MutableText text;
        if (!noIssues) {
            text = Text.translatable("gui.adorn.mod_compatibility_warning.found", violations.size());

            for (var violation : violations) {
                text.append("\n - ");
                text.append(
                    Text.translatable(
                        "gui.adorn.mod_compatibility_warning.enum_modification",
                        getTextForClass(violation.enumClass()),
                        Text.literal(String.join(", ", violation.additionalEntries()))
                    )
                );
            }
        } else {
            text = Text.translatable("gui.adorn.mod_compatibility_warning.no_issues");
        }

        int leftMargin = (width - CONTENT_WIDTH) / 2;
        var textElement = new MultilineTextElement(
            MultilineText.create(textRenderer, text, CONTENT_WIDTH),
            leftMargin,
            headerFooterOptions.backgroundY() + VERTICAL_PADDING,
            textRenderer.fontHeight + 1,
            Colors.WHITE
        );
        addDrawableChild(
            new ScrollEnvelope(
                leftMargin,
                headerFooterOptions.backgroundY(),
                CONTENT_WIDTH,
                headerFooterOptions.backgroundHeight(height),
                textElement,
                animationEngine,
                true
            )
        );

        int footerButtonY = height - headerFooterOptions.footerWidgetYFromBottom(AbstractConfigScreen.BUTTON_HEIGHT);

        if (noIssues || fromConfigScreen) {
            // Don't add the "Do not show this screen again" button
            addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, widget -> close())
                    .position(width / 2 - 100, footerButtonY)
                    .size(200, 20)
                    .build()
            );
        } else {
            addDrawableChild(
                ButtonWidget.builder(Text.translatable("gui.adorn.dont_show_again"), widget -> closeAndDoNotShowAgain())
                    .position(width / 2 - 151, footerButtonY)
                    .size(150, 20)
                    .build()
            );
            addDrawableChild(
                ButtonWidget.builder(ScreenTexts.OK, widget -> close())
                    .position(width / 2 + 1, footerButtonY)
                    .size(150, 20)
                    .build()
            );
        }
    }

    private static Text getTextForClass(Class<?> c) {
        return Text.literal(c.getSimpleName())
            .setStyle(
                Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(c.getName())))
                    .withColor(Formatting.GREEN)
            );
    }

    private void closeAndDoNotShowAgain() {
        ConfigManager.config().checkModCompatIssues = CompatCheckMode.LOG_ONLY;
        ConfigManager.get().save();
        close();
    }

    @Override
    public void close() {
        client.setScreen(previous);
    }

    @Override
    public void removed() {
        animationEngine.stop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        headerFooterOptions.renderTitle(context, title, width);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        headerFooterOptions.renderBackground(context, width, height);
    }
}
