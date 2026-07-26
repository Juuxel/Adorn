package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.client.gui.widget.BrewingRecipeBookWidget;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.Logging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import org.slf4j.Logger;

import java.util.List;

public final class BrewerScreen extends AbstractRecipeBookScreen<BrewerMenu> {
    private static final Logger LOGGER = Logging.logger();
    public static final Identifier TEXTURE = AdornCommon.id("textures/gui/brewer.png");
    public static final int FLUID_AREA_HEIGHT = 59;

    public BrewerScreen(BrewerMenu menu, Inventory playerInventory, Component title) {
        super(menu, new BrewingRecipeBookWidget(menu), playerInventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float a) {
        super.extractBackground(context, mouseX, mouseY, a);
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0f, 0f, imageWidth, imageHeight, 256, 256);
        drawFluid(context, leftPos + 145, topPos + 17, menu.getFluid());
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 145, topPos + 21, 176, 25, 16, 51, 256, 256);

        var progress = menu.getProgress();
        if (progress > 0) {
            float progressFract = (float) progress / (float) BrewerBlockEntity.MAX_PROGRESS;
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos + 84, topPos + 24, 176, 0, 8, Mth.ceil(progressFract * 25), 256, 256);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor context, int x, int y) {
        super.extractTooltip(context, x, y);
        int x2 = x - this.leftPos;
        int y2 = y - this.topPos;
        if (145 <= x2 && x2 < 145 + 16 && 17 <= y2 && y2 < 17 + FLUID_AREA_HEIGHT) {
            context.setComponentTooltipForNextFrame(font, getFluidTooltip(menu.getFluid()), x, y);
        }
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(leftPos + 16, height / 2 - 49);
    }

    private List<Component> getFluidTooltip(FluidReference fluid) {
        return FluidRenderingBridge.get().getTooltip(
            fluid,
            minecraft.options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL,
            BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * 1000
        );
    }

    public static void setFluidFromPacket(Minecraft client, int syncId, FluidVolume fluid) {
        if (client.screen instanceof MenuAccess<?> menuProvider) {
            var menu = menuProvider.getMenu();
            if (menu.containerId == syncId && menu instanceof BrewerMenu brewerMenu) {
                brewerMenu.setFluid(fluid);
            }
        }
    }

    private static void drawSprite(GuiGraphicsExtractor context, int x, int y, int width, int height, float u0, float v0, float u1, float v1, TextureAtlasSprite sprite, int color) {
        var au0 = Mth.lerp(u0, sprite.getU0(), sprite.getU1());
        var au1 = Mth.lerp(u1, sprite.getU0(), sprite.getU1());
        var av0 = Mth.lerp(v0, sprite.getV0(), sprite.getV1());
        var av1 = Mth.lerp(v1, sprite.getV0(), sprite.getV1());
        context.innerBlit(RenderPipelines.GUI_TEXTURED, sprite.atlasLocation(), x, x + width, y, y + height, au0, au1, av0, av1, color);
    }

    public static void drawFluid(GuiGraphicsExtractor context, int x, int y, FluidReference fluid) {
        if (fluid.isEmpty()) return;

        var bridge = FluidRenderingBridge.get();
        var sprite = bridge.getStillSprite(fluid);
        if (sprite == null) {
            LOGGER.warn("Could not find sprite for {} in brewer screen", fluid);
            return;
        }

        var color = Colors.color(bridge.getColor(fluid));
        var height = Math.round(FLUID_AREA_HEIGHT * (fluid.getAmount() / (float) (BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * fluid.getUnit().getBucketVolume())));
        var fluidY = 0;

        int tiles = height / 16;
        for (int i = 0; i < tiles; i++) {
            drawSprite(context, x, y + transformY(bridge, fluid, fluidY, 16), 16, 16, 0f, 0f, 1f, 1f, sprite, color);
            fluidY += 16;
        }

        var leftover = height % 16;
        drawSprite(context, x, y + transformY(bridge, fluid, fluidY, leftover), 16, leftover, 0f, 0f, 1f, leftover / 16f, sprite, color);
    }

    private static int transformY(FluidRenderingBridge bridge, FluidReference fluid, int fluidY, int areaHeight) {
        if (bridge.fillsFromTop(fluid)) {
            return fluidY;
        } else {
            return FLUID_AREA_HEIGHT - fluidY - areaHeight;
        }
    }
}
