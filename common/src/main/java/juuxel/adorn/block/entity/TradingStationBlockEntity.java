package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.trading.TradeOwner;
import juuxel.adorn.util.AdornUtil;
import juuxel.adorn.util.InventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.menu.NamedMenuFactory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class TradingStationBlockEntity extends BlockEntity implements NamedMenuFactory, TradingStation {
    public static final int STORAGE_SIZE = 12;
    public static final String NBT_TRADING_OWNER = "TradingOwner";
    public static final String NBT_TRADING_OWNER_NAME = "TradingOwnerName";
    public static final String NBT_TRADE = "Trade";
    public static final String NBT_STORAGE = "Storage";
    public static final Text UNKNOWN_OWNER = Text.literal("???");

    private @Nullable UUID owner = null;
    private Text ownerName = UNKNOWN_OWNER;
    private final Trade trade = Trade.empty();
    private final InventoryComponent storage = new InventoryComponent(STORAGE_SIZE);

    public TradingStationBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.TRADING_STATION.get(), pos, state);

        trade.addListener(sender -> markDirty());
        storage.addListener(sender -> markDirty());
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public void setOwner(PlayerEntity player) {
        owner = player.getGameProfile().id();
        ownerName = Text.literal(player.getGameProfile().name());
        markDirty();
    }

    public void setOwnerIfMissing(PlayerEntity player) {
        if (owner == null) {
            setOwner(player);
        }
    }

    public boolean isStorageStocked() {
        return storage.getCountWithComponents(trade.getSelling()) >= trade.getSelling().getCount();
    }

    public boolean isOwner(PlayerEntity player) {
        return player.getGameProfile().id().equals(owner);
    }

    @Override
    public Menu createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TradingStationMenu(syncId, playerInventory, AdornUtil.menuContextOf(this));
    }

    @Override
    public Text getDisplayName() {
        return getCachedState().getBlock().getName();
    }

    @Override
    public Text getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(Text ownerName) {
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
    protected void readData(ReadView view) {
        super.readData(view);

        owner = view.read(NBT_TRADING_OWNER, Uuids.INT_STREAM_CODEC).orElse(null);
        ownerName = view.read(NBT_TRADING_OWNER_NAME, TextCodecs.CODEC).orElse(UNKNOWN_OWNER);
        trade.readData(view.getReadView(NBT_TRADE));
        storage.readData(view.getReadView(NBT_STORAGE));
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);

        view.putNullable(NBT_TRADING_OWNER, Uuids.INT_STREAM_CODEC, owner);
        view.put(NBT_TRADING_OWNER_NAME, TextCodecs.CODEC, ownerName);

        trade.writeData(view.get(NBT_TRADE));
        storage.writeData(view.get(NBT_STORAGE));
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createComponentlessNbt(registries);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);

        trade.copyFrom(components.get(AdornComponentTypes.TRADE.get()));
        storage.copyFrom(components.get(DataComponentTypes.CONTAINER));

        var owner = components.get(AdornComponentTypes.TRADE_OWNER.get());
        if (owner != null) {
            this.owner = owner.uuid();
            this.ownerName = owner.name();
        }
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);

        builder.add(AdornComponentTypes.TRADE.get(), trade);
        builder.add(DataComponentTypes.CONTAINER, storage.toContainerComponent());

        if (owner != null) {
            builder.add(AdornComponentTypes.TRADE_OWNER.get(), new TradeOwner(owner, ownerName));
        }
    }

    @Override
    public void removeFromCopiedStackData(WriteView view) {
        super.removeFromCopiedStackData(view);
        view.remove(NBT_TRADE);
        view.remove(NBT_STORAGE);
        view.remove(NBT_TRADING_OWNER);
        view.remove(NBT_TRADING_OWNER_NAME);
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        if (world instanceof ServerWorld serverWorld && !serverWorld.getGameRules().getValue(AdornGameRules.DROP_LOCKED_TRADING_STATIONS.get())) {
            ItemScatterer.spawn(world, pos, getStorage());
        }
    }
}
