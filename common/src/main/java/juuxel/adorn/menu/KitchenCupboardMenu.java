package juuxel.adorn.menu;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.menu.MenuContext;
import net.minecraft.util.math.BlockPos;

public final class KitchenCupboardMenu extends SimpleMenu {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;

    public KitchenCupboardMenu(int syncId, PlayerInventory playerInventory, Inventory inventory, MenuContext context) {
        super(AdornMenus.KITCHEN_CUPBOARD.get(), syncId, WIDTH, HEIGHT, inventory, playerInventory, context);
    }

    public static KitchenCupboardMenu load(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        var context = MenuContext.create(playerInventory.player.getEntityWorld(), pos);
        return new KitchenCupboardMenu(syncId, playerInventory, new SimpleInventory(WIDTH * HEIGHT), context);
    }
}
