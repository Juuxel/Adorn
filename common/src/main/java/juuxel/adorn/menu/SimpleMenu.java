package juuxel.adorn.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class SimpleMenu extends AbstractContainerMenu implements ContainerBlockMenu {
    private final int width;
    private final int height;
    private final Container inventory;
    private final ContainerLevelAccess context;

    public SimpleMenu(MenuType<?> type, int syncId, int width, int height, Container inventory, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId);
        this.width = width;
        this.height = height;
        this.inventory = inventory;
        this.context = context;

        int offset = (9 - width) / 2;
        checkContainerSize(inventory, width * height);

        int slot = 18;

        // Container
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                addSlot(new Slot(inventory, y * width + x, 8 + (x + offset) * slot, 17 + y * slot));
            }
        }

        // Main player inventory
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 8; x++) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * slot, 84 + y * slot));
            }
        }

        // Hotbar
        for (int x = 0; x <= 8; x++) {
            addSlot(new Slot(playerInventory, x, 8 + x * slot, 142));
        }
    }

    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    public ContainerLevelAccess getContext() {
        return context;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            var containerSize = width * height;
            var stack = slot.getItem();
            result = stack.copy();

            if (index < containerSize) {
                if (!moveItemStackTo(stack, containerSize, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 0, containerSize, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }
}
