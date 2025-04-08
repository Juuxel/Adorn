package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.menu.ContainerBlockMenu;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class PalettedMenuScreen<M extends Menu & ContainerBlockMenu> extends AdornMenuScreen<M> {
    private final Identifier blockId;

    public PalettedMenuScreen(M menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
        this.blockId = Registries.BLOCK.getId(menu.getContext().get((world, pos) -> world.getBlockState(pos).getBlock(), Blocks.AIR));
    }

    protected abstract Identifier getBackgroundTexture();
    protected abstract Identifier getPaletteId();

    private ColorManager.ColorPair getPalette() {
        return PlatformBridges.get().getResources().getColorManager().getColors(getPaletteId()).get(blockId);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        var bg = getPalette().bg();
        context.drawTexture(RenderLayer::getGuiTextured, getBackgroundTexture(), x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256, bg);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        var fg = getPalette().fg();
        context.drawText(textRenderer, title, titleX, titleY, fg, false);
        context.drawText(textRenderer, playerInventoryTitle, playerInventoryTitleX, playerInventoryTitleY, fg, false);
    }
}
