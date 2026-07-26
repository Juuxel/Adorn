package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public final class CheckTradingStationOwnerLootFunction implements LootItemFunction {
    public static final CheckTradingStationOwnerLootFunction INSTANCE = new CheckTradingStationOwnerLootFunction();
    public static final Builder BUILDER = new Builder();
    public static final MapCodec<CheckTradingStationOwnerLootFunction> CODEC = MapCodec.unit(INSTANCE);

    private CheckTradingStationOwnerLootFunction() {
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext lootContext) {
        if (stack.is(AdornBlocks.TRADING_STATION.get().asItem())) {
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
        var container = stack.get(DataComponents.CONTAINER);
        if (container == null) return false;
        return container.nonEmptyItems().iterator().hasNext();
    }

    private void clearOwner(ItemStack stack) {
        stack.remove(AdornComponentTypes.TRADE_OWNER.get());
    }

    @Override
    public MapCodec<? extends LootItemFunction> codec() {
        return CODEC;
    }

    public static final class Builder implements LootItemFunction.Builder {
        private Builder() {
        }

        @Override
        public LootItemFunction build() {
            return INSTANCE;
        }
    }
}
