package juuxel.adorn.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuContext;
import net.minecraft.menu.MenuType;
import net.minecraft.menu.slot.Slot;

public abstract class SimpleMenu extends Menu implements ContainerBlockMenu {
    private final int width;
    private final int height;
    private final Inventory inventory;
    private final MenuContext context;

    public SimpleMenu(MenuType<?> type, int syncId, int width, int height, Inventory inventory, PlayerInventory playerInventory, MenuContext context) {
        super(type, syncId);
        this.width = width;
        this.height = height;
        this.inventory = inventory;
        this.context = context;

        int offset = (9 - width) / 2;
        checkSize(inventory, width * height);

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
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public MenuContext getContext() {
        return context;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot != null && slot.hasStack()) {
            var containerSize = width * height;
            var stack = slot.getStack();
            result = stack.copy();

            if (index < containerSize) {
                if (!insertItem(stack, containerSize, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(stack, 0, containerSize, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return result;
    }
}
