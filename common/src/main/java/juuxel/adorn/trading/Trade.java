package juuxel.adorn.trading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.DataConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trade implements DataConvertible, TooltipData {
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
    public void readData(ReadView view) {
        selling = view.read(NBT_SELLING, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        price = view.read(NBT_PRICE, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    public void writeData(WriteView view) {
        view.put(NBT_SELLING, ItemStack.OPTIONAL_CODEC, selling);
        view.put(NBT_PRICE, ItemStack.OPTIONAL_CODEC, price);
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

    public static Trade fromDataView(ReadView view) {
        var trade = empty();
        trade.readData(view);
        return trade;
    }

    @FunctionalInterface
    public interface TradeListener {
        void onTradeChanged(Trade trade);
    }
}
