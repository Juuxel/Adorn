package juuxel.adorn.client.gui.screen;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.menu.KitchenCupboardMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class KitchenCupboardScreen extends PalettedMenuScreen<KitchenCupboardMenu> {
    private static final Identifier BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/kitchen_cupboard.png");
    private static final Identifier PALETTE_ID = AdornCommon.id("kitchen_cupboard");

    public KitchenCupboardScreen(KitchenCupboardMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return BACKGROUND_TEXTURE;
    }

    @Override
    protected Identifier getPaletteId() {
        return PALETTE_ID;
    }
}
