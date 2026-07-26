package juuxel.adorn.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;

public final class DrawerMenu extends SimpleMenu {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 3;

    public DrawerMenu(int syncId, Inventory playerInventory, Container inventory, ContainerLevelAccess context) {
        super(AdornMenus.DRAWER.get(), syncId, WIDTH, HEIGHT, inventory, playerInventory, context);
    }

    public static DrawerMenu load(int syncId, Inventory playerInventory, BlockPos pos) {
        var context = ContainerLevelAccess.create(playerInventory.player.level(), pos);
        return new DrawerMenu(syncId, playerInventory, new SimpleContainer(WIDTH * HEIGHT), context);
    }
}
