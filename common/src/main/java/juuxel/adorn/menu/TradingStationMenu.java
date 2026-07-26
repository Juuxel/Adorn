package juuxel.adorn.menu;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.entity.TradingStation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;

public final class TradingStationMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess context;
    private final TradingStation tradingStation;
    private final Slot sellingSlot;
    private final Slot priceSlot;

    public TradingStationMenu(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
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

    public TradingStationMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL);
    }

    /**
     * Gets the {@link juuxel.adorn.block.entity.TradingStationBlockEntity} at the context's location.
     * If it's not present, creates an empty trading station using {@link TradingStation#createEmpty()}.
     */
    private static TradingStation getTradingStation(ContainerLevelAccess context) {
        return context.evaluate((world, pos) -> world.getBlockEntity(pos, AdornBlockEntities.TRADING_STATION.get()))
            .<TradingStation>flatMap(tradingStation -> tradingStation)
            .orElseGet(TradingStation::createEmpty);
    }

    public static boolean isValidItem(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    public Slot getSellingSlot() {
        return sellingSlot;
    }

    public Slot getPriceSlot() {
        return priceSlot;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(context, player, AdornBlocks.TRADING_STATION.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var offset = 2;

        // Ghost slots
        if (index < offset) return ItemStack.EMPTY;

        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasItem()) {
            var containerSize = 12;
            var stack = slot.getItem();
            result = stack.copy();

            if (offset <= index && index < containerSize + offset) {
                if (!moveItemStackTo(stack, containerSize + offset, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, offset, containerSize + offset, false)) {
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

    @Override
    public void clicked(int slotIndex, int button, ClickType actionType, Player player) {
        var slot = 0 <= slotIndex && slotIndex < slots.size() ? slots.get(slotIndex) : null;

        if (actionType == ClickType.PICKUP && slot instanceof TradeSlot tradeSlot) {
            var cursorStack = getCarried();
            if (isValidItem(cursorStack)) {
                updateTradeStack(tradeSlot, cursorStack.copy(), player);
            }
        } else {
            super.clicked(slotIndex, button, actionType, player);
        }
    }

    public void updateTradeStack(int slotId, ItemStack stack, Player player) {
        if (getSlot(slotId) instanceof TradeSlot slot) {
            updateTradeStack(slot, stack, player);
        }
    }

    private void updateTradeStack(TradeSlot slot, ItemStack stack, Player player) {
        slot.setByPlayer(stack);
        slot.setChanged();

        if (tradingStation instanceof BlockEntity be) {
            var state = be.getBlockState();
            player.level().sendBlockUpdated(be.getBlockPos(), state, state, Block.UPDATE_CLIENTS);
        }
    }

    private static final class TradeSlot extends Slot {
        private TradeSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPickup(Player playerEntity) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack remove(int amount) {
            return ItemStack.EMPTY;
        }
    }

    private static final class StorageSlot extends Slot {
        private StorageSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return isValidItem(stack);
        }
    }
}
