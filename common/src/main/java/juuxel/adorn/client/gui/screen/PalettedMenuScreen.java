package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.menu.ContainerBlockMenu;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class PalettedMenuScreen<M extends AbstractContainerMenu & ContainerBlockMenu> extends AdornMenuScreen<M> {
    private final Identifier blockId;

    public PalettedMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.blockId = BuiltInRegistries.BLOCK.getKey(menu.getContext().evaluate((world, pos) -> world.getBlockState(pos).getBlock(), Blocks.AIR));
    }

    protected abstract Identifier getBackgroundTexture();
    protected abstract Identifier getPaletteId();

    private ColorManager.ColorPair getPalette() {
        return PlatformBridges.get().getResources().getColorManager().getColors(getPaletteId()).get(blockId);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        var bg = getPalette().bg();
        context.blit(RenderPipelines.GUI_TEXTURED, getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256, bg);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        var fg = getPalette().fg();
        context.drawString(font, title, titleLabelX, titleLabelY, fg, false);
        context.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, fg, false);
    }
}
