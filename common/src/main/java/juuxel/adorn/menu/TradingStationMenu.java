package juuxel.adorn.menu;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.entity.TradingStation;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuContext;
import net.minecraft.menu.slot.Slot;
import net.minecraft.menu.slot.SlotActionType;

public final class TradingStationMenu extends Menu {
    private final MenuContext context;
    private final TradingStation tradingStation;
    private final Slot sellingSlot;
    private final Slot priceSlot;

    public TradingStationMenu(int syncId, PlayerInventory playerInventory, MenuContext context) {
        super(AdornMenus.TRADING_STATION.get(), syncId);
        this.context = context;

        var slot = 18;

        tradingStation = getTradingStation(context);
        var tradeInventory = tradingStation.getTrade().createInventory();
        var storage = tradingStation.getStorage();

        sellingSlot = addSlot(new TradeSlot(tradeInventory, 0, 26, 36));
        priceSlot = addSlot(new TradeSlot(tradeInventory, 1, 26, 72));

        // Storage
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 3; x++) {
                addSlot(new StorageSlot(storage, x + y * 4, 62 + x * slot, 36 + y * slot));
            }
        }

        // Main player inventory
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 8; x++) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * slot, 104 + y * slot));
            }
        }

        // Hotbar
        for (int x = 0; x <= 8; x++) {
            addSlot(new Slot(playerInventory, x, 8 + x * slot, 162));
        }
    }

    public TradingStationMenu(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, MenuContext.EMPTY);
    }

    /**
     * Gets the {@link juuxel.adorn.block.entity.TradingStationBlockEntity} at the context's location.
     * If it's not present, creates an empty trading station using {@link TradingStation#createEmpty()}.
     */
    private static TradingStation getTradingStation(MenuContext context) {
        return context.get((world, pos) -> world.getBlockEntity(pos, AdornBlockEntities.TRADING_STATION.get()))
            .<TradingStation>flatMap(tradingStation -> tradingStation)
            .orElseGet(TradingStation::createEmpty);
    }

    public static boolean isValidItem(ItemStack stack) {
        return stack.getItem().canBeNested();
    }

    public Slot getSellingSlot() {
        return sellingSlot;
    }

    public Slot getPriceSlot() {
        return priceSlot;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, AdornBlocks.TRADING_STATION.get());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        var offset = 2;

        // Ghost slots
        if (index < offset) return ItemStack.EMPTY;

        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasStack()) {
            var containerSize = 12;
            var stack = slot.getStack();
            result = stack.copy();

            if (offset <= index && index < containerSize + offset) {
                if (!insertItem(stack, containerSize + offset, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(stack, offset, containerSize + offset, false)) {
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

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        var slot = 0 <= slotIndex && slotIndex < slots.size() ? slots.get(slotIndex) : null;

        if (actionType == SlotActionType.PICKUP && slot instanceof TradeSlot tradeSlot) {
            var cursorStack = getCursorStack();
            if (isValidItem(cursorStack)) {
                updateTradeStack(tradeSlot, cursorStack.copy(), player);
            }
        } else {
            super.onSlotClick(slotIndex, button, actionType, player);
        }
    }

    public void updateTradeStack(int slotId, ItemStack stack, PlayerEntity player) {
        if (getSlot(slotId) instanceof TradeSlot slot) {
            updateTradeStack(slot, stack, player);
        }
    }

    private void updateTradeStack(TradeSlot slot, ItemStack stack, PlayerEntity player) {
        slot.setStack(stack);
        slot.markDirty();

        if (tradingStation instanceof BlockEntity be) {
            var state = be.getCachedState();
            player.getWorld().updateListeners(be.getPos(), state, state, Block.NOTIFY_LISTENERS);
        }
    }

    private static final class TradeSlot extends Slot {
        private TradeSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return false;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack takeStack(int amount) {
            return ItemStack.EMPTY;
        }
    }

    private static final class StorageSlot extends Slot {
        private StorageSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return isValidItem(stack);
        }
    }
}
