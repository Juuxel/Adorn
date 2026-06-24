package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;

public final class CheckTradingStationOwnerLootFunction implements LootFunction {
    public static final CheckTradingStationOwnerLootFunction INSTANCE = new CheckTradingStationOwnerLootFunction();
    public static final Builder BUILDER = new Builder();
    public static final MapCodec<CheckTradingStationOwnerLootFunction> CODEC = MapCodec.unit(INSTANCE);

    private CheckTradingStationOwnerLootFunction() {
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext) {
        if (stack.isOf(AdornBlocks.TRADING_STATION.get().asItem())) {
            if (!hasTrade(stack) && !hasStorage(stack)) {
                clearOwner(stack);
            }
        }

        return stack;
    }

    private boolean hasTrade(ItemStack stack) {
        var trade = stack.get(AdornComponentTypes.TRADE.get());
        return trade != null && !trade.isFullyEmpty();
    }

    private boolean hasStorage(ItemStack stack) {
        var container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) return false;
        return container.streamNonEmpty().findAny().isPresent();
    }

    private void clearOwner(ItemStack stack) {
        stack.remove(AdornComponentTypes.TRADE_OWNER.get());
    }

    @Override
    public LootFunctionType<? extends LootFunction> getType() {
        return AdornLootFunctionTypes.CHECK_TRADING_STATION_OWNER.get();
    }

    public static final class Builder implements LootFunction.Builder {
        private Builder() {
        }

        @Override
        public LootFunction build() {
            return INSTANCE;
        }
    }
}
