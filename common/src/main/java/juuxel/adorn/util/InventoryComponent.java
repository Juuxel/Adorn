package juuxel.adorn.util;

import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent implements Inventory, DataConvertible {
    private final int size;
    private final List<InventoryChangedListener> listeners = new ArrayList<>();
    private final DefaultedList<ItemStack> items;

    public InventoryComponent(int size) {
        this.size = size;
        items = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    private InventoryComponent(DefaultedList<ItemStack> items) {
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
            if (ItemStack.areItemsEqual(invStack, stack)) {
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
            if (ItemStack.areItemsAndComponentsEqual(invStack, stack)) {
                int invStackAmount = invStack.getCount();
                invStack.decrement(Math.min(invStackAmount, remainingAmount));
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
            if (ItemStack.areItemsAndComponentsEqual(invStack, stack) && invStack.getCount() < invStack.getMaxCount()) {
                int insertionAmount = Math.min(invStack.getMaxCount() - invStack.getCount(), remainingAmount);
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
            if (ItemStack.areItemsAndComponentsEqual(invStack, stack) && invStack.getCount() < invStack.getMaxCount()) {
                int insertionAmount = Math.min(invStack.getMaxCount() - invStack.getCount(), remainingAmount);
                remainingAmount -= insertionAmount;
                invStack.increment(insertionAmount);
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
        return CollectionUtil.sumOf(items, it -> ItemStack.areItemsAndComponentsEqual(stack, it) ? it.getCount() : 0);
    }

    // ------
    // Data
    // ------

    @Override
    public void writeData(WriteView view) {
        Inventories.writeData(view, items);
    }

    @Override
    public void readData(ReadView view) {
        Inventories.readData(view, items);
    }

    public ContainerComponent toContainerComponent() {
        return ContainerComponent.fromStacks(items);
    }

    public void copyFrom(@Nullable ContainerComponent component) {
        if (component == null) return;
        component.copyTo(items);
    }

    // -------------------------------
    // Inventory management/transfer
    // -------------------------------

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        var stack = Inventories.splitStack(items, slot, count);
        if (!stack.isEmpty()) markDirty();
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
    public void markDirty() {
        for (var listener : listeners) {
            listener.onInventoryChanged(this);
        }
    }

    public void addListener(InventoryChangedListener listener) {
        listeners.add(listener);
    }
}
