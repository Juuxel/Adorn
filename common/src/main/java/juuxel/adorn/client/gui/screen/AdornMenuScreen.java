package juuxel.adorn.client.gui.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AdornMenuScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {
    public AdornMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public AdornMenuScreen(M menu, Inventory playerInventory, Component title, int imageWidth, int imageHeight) {
        super(menu, playerInventory, title, imageWidth, imageHeight);
    }

    public int getPanelX() {
        return leftPos;
    }

    public int getPanelY() {
        return topPos;
    }
}
