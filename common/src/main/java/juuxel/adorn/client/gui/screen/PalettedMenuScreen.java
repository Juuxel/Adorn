package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.menu.ContainerBlockMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Blocks;

public abstract class PalettedMenuScreen<M extends AbstractContainerMenu & ContainerBlockMenu> extends AdornMenuScreen<M> {
    private final Identifier blockId;

    public PalettedMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.blockId = BuiltInRegistries.BLOCK.getKey(menu.getContext().evaluate((world, pos) -> world.getBlockState(pos).getBlock(), Blocks.AIR));
    }

    protected abstract Identifier getBackgroundTexture();
    protected abstract Identifier getPaletteId();

    private ColorManager.ColorPair getPalette() {
        return ColorManager.INSTANCE.getColors(getPaletteId()).get(blockId);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float a) {
        super.extractBackground(context, mouseX, mouseY, a);
        var bg = getPalette().bg();
        context.blit(RenderPipelines.GUI_TEXTURED, getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256, bg);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        var fg = getPalette().fg();
        context.text(font, title, titleLabelX, titleLabelY, fg, false);
        context.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, fg, false);
    }
}
