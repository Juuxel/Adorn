package juuxel.adorn.compat.rei.client;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.compat.rei.AdornReiServer;
import juuxel.adorn.compat.rei.BrewerDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class BrewerCategory implements DisplayCategory<BrewerDisplay> {
    private static final Identifier LIGHT_TEXTURE = AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png");
    private static final Identifier DARK_TEXTURE = AdornCommon.id("textures/gui/recipe_viewer/brewer_dark.png");

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(AdornBlocks.BREWER.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.adorn.brewer");
    }

    @Override
    public CategoryIdentifier<? extends BrewerDisplay> getCategoryIdentifier() {
        return AdornReiServer.BREWER;
    }

    @Override
    public int getDisplayHeight() {
        return 72;
    }

    @Override
    public List<Widget> setupDisplay(BrewerDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        var topLeft = new Point(bounds.getCenterX() - 39, bounds.getCenterY() - 30);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(
            Widgets.createDrawableWidget((context, _0, _1, _2) -> {
                context.blit(RenderPipelines.GUI_TEXTURED, currentTexture(), topLeft.x, topLeft.y, 49, 16, 105, 61, 256, 256);
                float progressFraction = (System.currentTimeMillis() % 4000) / 4000f;
                int height = Math.round(progressFraction * 25);
                context.blit(RenderPipelines.GUI_TEXTURED, currentTexture(), topLeft.x + 35, topLeft.y + 8, 176, 0, 8, height, 256, 256);
            })
        );
        widgets.add(
            Widgets.createSlot(new Point(topLeft.x + 1, topLeft.y + 1))
                .disableBackground()
                .markInput()
                .entries(display.first())
        );
        widgets.add(
            Widgets.createSlot(new Point(topLeft.x + 61, topLeft.y + 1))
                .disableBackground()
                .markInput()
                .entries(display.second())
        );
        widgets.add(
            Widgets.createSlot(new Point(topLeft.x + 31, topLeft.y + 40))
                .disableBackground()
                .markOutput()
                .entry(display.result())
        );
        widgets.add(
            Widgets.createSlot(new Rectangle(topLeft.x + 87, topLeft.y, 18, BrewerScreen.FLUID_AREA_HEIGHT + 2))
                .disableBackground()
                .markInput()
                .entries(display.fluid())
        );
        widgets.add(
            Widgets.createSlot(new Point(topLeft.x + 4, topLeft.y + 39))
                .disableBackground()
                .markInput()
                .entries(display.input())
        );
        // Fluid scale for empty fluid slots
        widgets.add(
            Widgets.createDrawableWidget((context, _0, _1, _2) -> {
                context.pose().pushMatrix();
                context.pose().translate(0f, 0f);
                context.blit(RenderPipelines.GUI_TEXTURED, currentTexture(), topLeft.x + 88, topLeft.y + 1, 154, 17, 16, BrewerScreen.FLUID_AREA_HEIGHT, 256, 256);
                context.pose().popMatrix();
            })
        );
        return widgets;
    }

    private static Identifier currentTexture() {
        return REIRuntime.getInstance().isDarkThemeEnabled() ? DARK_TEXTURE : LIGHT_TEXTURE;
    }
}
