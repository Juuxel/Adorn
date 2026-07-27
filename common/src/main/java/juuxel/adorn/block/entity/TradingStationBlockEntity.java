package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.trading.TradeOwner;
import juuxel.adorn.util.AdornUtil;
import juuxel.adorn.util.InventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public final class TradingStationBlockEntity extends BlockEntity implements MenuProvider, TradingStation {
    public static final int STORAGE_SIZE = 12;
    public static final String NBT_TRADING_OWNER = "TradingOwner";
    public static final String NBT_TRADING_OWNER_NAME = "TradingOwnerName";
    public static final String NBT_TRADE = "Trade";
    public static final String NBT_STORAGE = "Storage";
    public static final Component UNKNOWN_OWNER = Component.literal("???");

    private @Nullable UUID owner = null;
    private Component ownerName = UNKNOWN_OWNER;
    private final Trade trade = Trade.empty();
    private final InventoryComponent storage = new InventoryComponent(STORAGE_SIZE);

    public TradingStationBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.TRADING_STATION.get(), pos, state);

        trade.addListener(_ -> setChanged());
        storage.addListener(this::setChanged);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public void setOwner(Player player) {
        owner = player.getGameProfile().id();
        ownerName = Component.literal(player.getGameProfile().name());
        setChanged();
    }

    public void setOwnerIfMissing(Player player) {
        if (owner == null) {
            setOwner(player);
        }
    }

    public boolean isStorageStocked() {
        return storage.getCountWithComponents(trade.getSelling()) >= trade.getSelling().getCount();
    }

    public boolean isOwner(Player player) {
        return player.getGameProfile().id().equals(owner);
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new TradingStationMenu(syncId, playerInventory, AdornUtil.menuContextOf(this));
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public Component getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(Component ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public Trade getTrade() {
        return trade;
    }

    @Override
    public InventoryComponent getStorage() {
        return storage;
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);

        owner = view.read(NBT_TRADING_OWNER, UUIDUtil.CODEC).orElse(null);
        ownerName = view.read(NBT_TRADING_OWNER_NAME, ComponentSerialization.CODEC).orElse(UNKNOWN_OWNER);
        trade.readData(view.childOrEmpty(NBT_TRADE));
        storage.readData(view.childOrEmpty(NBT_STORAGE));
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);

        view.storeNullable(NBT_TRADING_OWNER, UUIDUtil.CODEC, owner);
        view.store(NBT_TRADING_OWNER_NAME, ComponentSerialization.CODEC, ownerName);

        trade.writeData(view.child(NBT_TRADE));
        storage.writeData(view.child(NBT_STORAGE));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);

        trade.copyFrom(components.get(AdornComponentTypes.TRADE.get()));
        storage.copyFrom(components.get(DataComponents.CONTAINER));

        var owner = components.get(AdornComponentTypes.TRADE_OWNER.get());
        if (owner != null) {
            this.owner = owner.uuid();
            this.ownerName = owner.name();
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);

        builder.set(AdornComponentTypes.TRADE.get(), trade);
        builder.set(DataComponents.CONTAINER, storage.toContainerComponent());

        if (owner != null) {
            builder.set(AdornComponentTypes.TRADE_OWNER.get(), new TradeOwner(owner, ownerName));
        }
    }

    @Override
    public void removeComponentsFromTag(ValueOutput view) {
        super.removeComponentsFromTag(view);
        view.discard(NBT_TRADE);
        view.discard(NBT_STORAGE);
        view.discard(NBT_TRADING_OWNER);
        view.discard(NBT_TRADING_OWNER_NAME);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
        if (level instanceof ServerLevel serverWorld && !serverWorld.getGameRules().get(AdornGameRules.DROP_LOCKED_TRADING_STATIONS.get())) {
            Containers.dropContents(level, pos, getStorage());
        }
    }
}
