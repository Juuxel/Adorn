package juuxel.adorn.menu;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.menu.MenuContext;
import net.minecraft.util.math.BlockPos;

public final class DrawerMenu extends SimpleMenu {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;

    public DrawerMenu(int syncId, PlayerInventory playerInventory, Inventory inventory, MenuContext context) {
        super(AdornMenus.DRAWER.get(), syncId, WIDTH, HEIGHT, inventory, playerInventory, context);
    }

    public static DrawerMenu load(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        var context = MenuContext.create(playerInventory.method_69259().getWorld(), pos);
        return new DrawerMenu(syncId, playerInventory, new SimpleInventory(WIDTH * HEIGHT), context);
    }
}
