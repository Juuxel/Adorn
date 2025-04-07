package juuxel.adorn.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.NbtConvertible;
import juuxel.adorn.util.NbtUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trade implements NbtConvertible, TooltipData {
    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.fieldOf("selling").forGetter(Trade::getSelling),
        ItemStack.OPTIONAL_CODEC.fieldOf("price").forGetter(Trade::getPrice)
    ).apply(instance, Trade::new));

    public static final String NBT_SELLING = "Selling";
    public static final String NBT_PRICE = "Price";

    private ItemStack selling;
    private ItemStack price;
    private final List<TradeListener> listeners = new ArrayList<>();

    public Trade(ItemStack selling, ItemStack price) {
        this.selling = selling;
        this.price = price;
    }

    public ItemStack getSelling() {
        return selling;
    }

    public void setSelling(ItemStack selling) {
        this.selling = selling;
    }

    public ItemStack getPrice() {
        return price;
    }

    public void setPrice(ItemStack price) {
        this.price = price;
    }

    public boolean isEmpty() {
        return selling.isEmpty() || price.isEmpty();
    }

    public boolean isFullyEmpty() {
        return selling.isEmpty() && price.isEmpty();
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        selling = NbtUtil.getWithCodec(nbt, NBT_SELLING, ItemStack.OPTIONAL_CODEC, registries, ItemStack.EMPTY);
        price = NbtUtil.getWithCodec(nbt, NBT_PRICE, ItemStack.OPTIONAL_CODEC, registries, ItemStack.EMPTY);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtUtil.putWithCodec(nbt, NBT_SELLING, ItemStack.OPTIONAL_CODEC, selling, registries);
        NbtUtil.putWithCodec(nbt, NBT_PRICE, ItemStack.OPTIONAL_CODEC, price, registries);
        return nbt;
    }

    public void copyFrom(@Nullable Trade trade) {
        if (trade == null) return;
        selling = trade.selling.copy();
        price = trade.price.copy();
    }

    public void addListener(TradeListener listener) {
        listeners.add(listener);
    }

    public void callListeners() {
        for (TradeListener listener : listeners) {
            listener.onTradeChanged(this);
        }
    }

    /**
     * Creates a modifiable inventory for this trade.
     */
    public TradeInventory createInventory() {
        return new TradeInventory(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selling, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Trade other)) return false;
        return selling.equals(other.selling) && price.equals(other.price);
    }

    public static Trade empty() {
        return new Trade(ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public static Trade fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        var trade = empty();
        trade.readNbt(nbt, registries);
        return trade;
    }

    @FunctionalInterface
    public interface TradeListener {
        void onTradeChanged(Trade trade);
    }
}
