package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.lib.AdornGameRules;
import juuxel.adorn.lib.registry.Registered;
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
        dropSelf(AdornBlocks.STONE_TORCH_GROUND);
        dropSelf(AdornBlocks.STONE_LADDER);
        dropSelf(AdornBlocks.CHAIN_LINK_FENCE);
        dropSelf(AdornBlocks.PICKET_FENCE);
        dropSelf(AdornBlocks.CRATE);
        dropSelf(AdornBlocks.APPLE_CRATE);
        dropSelf(AdornBlocks.WHEAT_CRATE);
        dropSelf(AdornBlocks.CARROT_CRATE);
        dropSelf(AdornBlocks.POTATO_CRATE);
        dropSelf(AdornBlocks.MELON_CRATE);
        dropSelf(AdornBlocks.WHEAT_SEED_CRATE);
        dropSelf(AdornBlocks.MELON_SEED_CRATE);
        dropSelf(AdornBlocks.PUMPKIN_SEED_CRATE);
        dropSelf(AdornBlocks.BEETROOT_CRATE);
        dropSelf(AdornBlocks.BEETROOT_SEED_CRATE);
        dropSelf(AdornBlocks.SWEET_BERRY_CRATE);
        dropSelf(AdornBlocks.COCOA_BEAN_CRATE);
        dropSelf(AdornBlocks.NETHER_WART_CRATE);
        dropSelf(AdornBlocks.SUGAR_CANE_CRATE);
        dropSelf(AdornBlocks.EGG_CRATE);
        dropSelf(AdornBlocks.HONEYCOMB_CRATE);
        dropSelf(AdornBlocks.LIL_TATER_CRATE);
        dropSelf(AdornBlocks.BREWER);
        dropSelf(AdornBlocks.BRICK_CHIMNEY);
        dropSelf(AdornBlocks.STONE_BRICK_CHIMNEY);
        dropSelf(AdornBlocks.NETHER_BRICK_CHIMNEY);
        dropSelf(AdornBlocks.RED_NETHER_BRICK_CHIMNEY);
        dropSelf(AdornBlocks.COBBLESTONE_CHIMNEY);
        dropSelf(AdornBlocks.PRISMARINE_CHIMNEY);
        dropSelf(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY);
        dropSelf(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY);
        dropSelf(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON));
        AdornBlocks.COPPER_PIPES.forEach(this::dropSelf);
        dropSelf(AdornBlocks.CANDLELIT_LANTERN);
        AdornBlocks.PAINTED_PLANKS.forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_SLABS.forEach(block -> add(block.get(), this::createSlabItemTable));
        AdornBlocks.PAINTED_WOOD_STAIRS.forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_FENCES.forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.forEach(this::dropSelf);
        AdornBlocks.PAINTED_WOOD_BUTTONS.forEach(this::dropSelf);
        dropSelf(AdornBlocks.BARRICADE);
        dropSelf(AdornBlocks.CAUTION_SIGN);
        dropSelf(AdornBlocks.BEE_CAUTION_SIGN);
        dropSelf(AdornBlocks.BOOK_CAUTION_SIGN);
        dropSelf(AdornBlocks.CLIFF_CAUTION_SIGN);
        dropSelf(AdornBlocks.FORBIDDEN_CAUTION_SIGN);
        dropSelf(AdornBlocks.HELMET_CAUTION_SIGN);
        dropSelf(AdornBlocks.RAILS_CAUTION_SIGN);
        dropSelf(AdornBlocks.SURPRISE_CAUTION_SIGN);
        add(AdornBlocks.TRADING_STATION.get(), this::tradingStationDrops);
    }

    private void dropSelf(Registered<? extends Block> block) {
        dropSelf(block.get());
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
