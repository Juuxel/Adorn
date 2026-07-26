package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.loot.CheckTradingStationOwnerLootFunction;
import juuxel.adorn.loot.GameRuleLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockLootTableGenerator extends FabricBlockLootSubProvider {
    public AdornBlockLootTableGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(AdornBlocks.STONE_TORCH_GROUND.get());
        dropSelf(AdornBlocks.STONE_LADDER.get());
        dropSelf(AdornBlocks.CHAIN_LINK_FENCE.get());
        dropSelf(AdornBlocks.PICKET_FENCE.get());
        dropSelf(AdornBlocks.CRATE.get());
        dropSelf(AdornBlocks.APPLE_CRATE.get());
        dropSelf(AdornBlocks.WHEAT_CRATE.get());
        dropSelf(AdornBlocks.CARROT_CRATE.get());
        dropSelf(AdornBlocks.POTATO_CRATE.get());
        dropSelf(AdornBlocks.MELON_CRATE.get());
        dropSelf(AdornBlocks.WHEAT_SEED_CRATE.get());
        dropSelf(AdornBlocks.MELON_SEED_CRATE.get());
        dropSelf(AdornBlocks.PUMPKIN_SEED_CRATE.get());
        dropSelf(AdornBlocks.BEETROOT_CRATE.get());
        dropSelf(AdornBlocks.BEETROOT_SEED_CRATE.get());
        dropSelf(AdornBlocks.SWEET_BERRY_CRATE.get());
        dropSelf(AdornBlocks.COCOA_BEAN_CRATE.get());
        dropSelf(AdornBlocks.NETHER_WART_CRATE.get());
        dropSelf(AdornBlocks.SUGAR_CANE_CRATE.get());
        dropSelf(AdornBlocks.EGG_CRATE.get());
        dropSelf(AdornBlocks.HONEYCOMB_CRATE.get());
        dropSelf(AdornBlocks.LIL_TATER_CRATE.get());
        dropSelf(AdornBlocks.BREWER.get());
        dropSelf(AdornBlocks.BRICK_CHIMNEY.get());
        dropSelf(AdornBlocks.STONE_BRICK_CHIMNEY.get());
        dropSelf(AdornBlocks.NETHER_BRICK_CHIMNEY.get());
        dropSelf(AdornBlocks.RED_NETHER_BRICK_CHIMNEY.get());
        dropSelf(AdornBlocks.COBBLESTONE_CHIMNEY.get());
        dropSelf(AdornBlocks.PRISMARINE_CHIMNEY.get());
        dropSelf(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.get());
        dropSelf(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.get());
        dropSelf(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get());
        dropSelf(AdornBlocks.COPPER_PIPE.get());
        dropSelf(AdornBlocks.EXPOSED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.WEATHERED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.OXIDIZED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.WAXED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get());
        dropSelf(AdornBlocks.CANDLELIT_LANTERN.get());
        AdornBlocks.PAINTED_PLANKS.values().forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_SLABS.values().forEach(block -> add(block, this::createSlabItemTable));
        AdornBlocks.PAINTED_WOOD_STAIRS.values().forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_FENCES.values().forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.values().forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.values().forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_BUTTONS.values().forEach(this::dropSelf);
        dropSelf(AdornBlocks.BARRICADE.get());
        dropSelf(AdornBlocks.CAUTION_SIGN.get());
        dropSelf(AdornBlocks.BEE_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.BOOK_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.CLIFF_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.FORBIDDEN_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.HELMET_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.RAILS_CAUTION_SIGN.get());
        dropSelf(AdornBlocks.SURPRISE_CAUTION_SIGN.get());
        add(AdornBlocks.TRADING_STATION.get(), this::tradingStationDrops);
    }

    private LootTable.Builder tradingStationDrops(Block block) {
        return LootTable.lootTable()
            .withPool(
                applyExplosionCondition(
                    block,
                    LootPool.lootPool()
                        .add(
                            LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                    .include(DataComponents.CONTAINER)
                                    .include(AdornComponentTypes.TRADE.get())
                                    .include(AdornComponentTypes.TRADE_OWNER.get())
                                    .when(GameRuleLootCondition.builder(AdornGameRules.DROP_LOCKED_TRADING_STATIONS)))
                                .apply(CheckTradingStationOwnerLootFunction.BUILDER)
                        )
                )
            );
    }
}
