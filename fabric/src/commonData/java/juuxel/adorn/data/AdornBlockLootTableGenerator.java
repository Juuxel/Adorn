package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.loot.CheckTradingStationOwnerLootFunction;
import juuxel.adorn.loot.GameRuleLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockLootTableGenerator extends FabricBlockLootTableProvider {
    public AdornBlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        AdornBlocks.PAINTED_PLANKS.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_SLABS.values().forEach(block -> addDrop(block, this::slabDrops));
        AdornBlocks.PAINTED_WOOD_STAIRS.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_BUTTONS.values().forEach(this::addDrop);
        addDrop(AdornBlocks.BARRICADE.get());
        addDrop(AdornBlocks.CAUTION_SIGN.get());
        addDrop(AdornBlocks.BEE_CAUTION_SIGN.get());
        addDrop(AdornBlocks.BOOK_CAUTION_SIGN.get());
        addDrop(AdornBlocks.CLIFF_CAUTION_SIGN.get());
        addDrop(AdornBlocks.FORBIDDEN_CAUTION_SIGN.get());
        addDrop(AdornBlocks.HELMET_CAUTION_SIGN.get());
        addDrop(AdornBlocks.RAILS_CAUTION_SIGN.get());
        addDrop(AdornBlocks.SURPRISE_CAUTION_SIGN.get());
        addDrop(AdornBlocks.TRADING_STATION.get(), this::tradingStationDrops);
    }

    private LootTable.Builder tradingStationDrops(Block block) {
        return LootTable.builder()
            .pool(
                addSurvivesExplosionCondition(
                    block,
                    LootPool.builder()
                        .with(
                            ItemEntry.builder(block)
                                .apply(CopyComponentsLootFunction.blockEntity(LootContextParameters.BLOCK_ENTITY)
                                    .include(DataComponentTypes.CONTAINER)
                                    .include(AdornComponentTypes.TRADE.get())
                                    .include(AdornComponentTypes.TRADE_OWNER.get())
                                    .conditionally(GameRuleLootCondition.builder(AdornGameRules.DROP_LOCKED_TRADING_STATIONS)))
                                .apply(CheckTradingStationOwnerLootFunction.BUILDER)
                        )
                )
            );
    }
}
