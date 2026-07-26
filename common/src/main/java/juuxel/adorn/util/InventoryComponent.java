package juuxel.adorn.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent implements Container, DataConvertible {
    private final int size;
    private final List<Runnable> listeners = new ArrayList<>();
    private final NonNullList<ItemStack> items;

    public InventoryComponent(int size) {
        this.size = size;
        items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    private InventoryComponent(NonNullList<ItemStack> items) {
        this(items.size());
        for (int i = 0; i < items.size(); i++) {
            this.items.set(i, items.get(i));
        }
    }

    /**
     * Creates a copy of this inventory, not retaining any listeners.
     */
    public InventoryComponent copy() {
        return new InventoryComponent(items);
    }

    /**
     * Checks if the stack can be extracted from this inventory. Ignores components.
     */
    public boolean canExtract(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.isSameItem(invStack, stack)) {
                remainingAmount -= invStack.getCount();
                if (remainingAmount <= 0) return true;
            }
        }

        return false;
    }

    /**
     * Tries to remove the stack from this inventory.
     *
     * @return {@code true} if extracted
     */
    public boolean tryExtract(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.isSameItemSameComponents(invStack, stack)) {
                int invStackAmount = invStack.getCount();
                invStack.shrink(Math.min(invStackAmount, remainingAmount));
                remainingAmount -= invStackAmount;
                if (remainingAmount <= 0) return true;
            }
        }

        return false;
    }

    /**
     * Checks if the stack can be inserted to this inventory.
     */
    public boolean canInsert(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (var invStack : items) {
            if (ItemStack.isSameItemSameComponents(invStack, stack) && invStack.getCount() < invStack.getMaxStackSize()) {
                int insertionAmount = Math.min(invStack.getMaxStackSize() - invStack.getCount(), remainingAmount);
                remainingAmount -= insertionAmount;
                if (remainingAmount <= 0) return true;
            } else if (invStack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tries to insert the stack to this inventory.
     *
     * @return {@code true} if inserted
     */
    public boolean tryInsert(ItemStack stack) {
        int remainingAmount = stack.getCount();

        for (int slot = 0; slot < items.size(); slot++) {
            var invStack = items.get(slot);
            if (ItemStack.isSameItemSameComponents(invStack, stack) && invStack.getCount() < invStack.getMaxStackSize()) {
                int insertionAmount = Math.min(invStack.getMaxStackSize() - invStack.getCount(), remainingAmount);
                remainingAmount -= insertionAmount;
                invStack.grow(insertionAmount);
                if (remainingAmount <= 0) return true;
            } else if (invStack.isEmpty()) {
                items.set(slot, stack.copy());
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the count of items with the same item and NBT as the stack.
     * Ignores the stack's count.
     */
    public int getCountWithComponents(ItemStack stack) {
        return CollectionUtil.sumOf(items, it -> ItemStack.isSameItemSameComponents(stack, it) ? it.getCount() : 0);
    }

    // ------
    // Data
    // ------

    @Override
    public void writeData(ValueOutput view) {
        ContainerHelper.saveAllItems(view, items);
    }

    @Override
    public void readData(ValueInput view) {
        ContainerHelper.loadAllItems(view, items);
    }

    public ItemContainerContents toContainerComponent() {
        return ItemContainerContents.fromItems(items);
    }

    public void copyFrom(@Nullable ItemContainerContents component) {
        if (component == null) return;
        component.copyInto(items);
    }

    // -------------------------------
    // Inventory management/transfer
    // -------------------------------

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        var stack = ContainerHelper.removeItem(items, slot, count);
        if (!stack.isEmpty()) setChanged();
        return stack;
    }

    @Override
    public boolean isEmpty() {
        for (var stack : items) {
            if (!stack.isEmpty()) return false;
        }

        return true;
    }

    // -----------
    // Listeners
    // -----------

    @Override
    public void setChanged() {
        for (var listener : listeners) {
            listener.run();
        }
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }
}
