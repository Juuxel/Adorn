package juuxel.adorn.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;

public abstract class AdornMenuScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {
    public AdornMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public int getPanelX() {
        return leftPos;
    }

    public int getPanelY() {
        return topPos;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
