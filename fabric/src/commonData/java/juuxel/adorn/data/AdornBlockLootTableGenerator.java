package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
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
        addDrop(AdornBlocks.STONE_TORCH_GROUND.get());
        addDrop(AdornBlocks.STONE_LADDER.get());
        addDrop(AdornBlocks.CHAIN_LINK_FENCE.get());
        addDrop(AdornBlocks.PICKET_FENCE.get());
        addDrop(AdornBlocks.CRATE.get());
        addDrop(AdornBlocks.APPLE_CRATE.get());
        addDrop(AdornBlocks.WHEAT_CRATE.get());
        addDrop(AdornBlocks.CARROT_CRATE.get());
        addDrop(AdornBlocks.POTATO_CRATE.get());
        addDrop(AdornBlocks.MELON_CRATE.get());
        addDrop(AdornBlocks.WHEAT_SEED_CRATE.get());
        addDrop(AdornBlocks.MELON_SEED_CRATE.get());
        addDrop(AdornBlocks.PUMPKIN_SEED_CRATE.get());
        addDrop(AdornBlocks.BEETROOT_CRATE.get());
        addDrop(AdornBlocks.BEETROOT_SEED_CRATE.get());
        addDrop(AdornBlocks.SWEET_BERRY_CRATE.get());
        addDrop(AdornBlocks.COCOA_BEAN_CRATE.get());
        addDrop(AdornBlocks.NETHER_WART_CRATE.get());
        addDrop(AdornBlocks.SUGAR_CANE_CRATE.get());
        addDrop(AdornBlocks.EGG_CRATE.get());
        addDrop(AdornBlocks.HONEYCOMB_CRATE.get());
        addDrop(AdornBlocks.LIL_TATER_CRATE.get());
        addDrop(AdornBlocks.BREWER.get());
        addDrop(AdornBlocks.BRICK_CHIMNEY.get());
        addDrop(AdornBlocks.STONE_BRICK_CHIMNEY.get());
        addDrop(AdornBlocks.NETHER_BRICK_CHIMNEY.get());
        addDrop(AdornBlocks.RED_NETHER_BRICK_CHIMNEY.get());
        addDrop(AdornBlocks.COBBLESTONE_CHIMNEY.get());
        addDrop(AdornBlocks.PRISMARINE_CHIMNEY.get());
        addDrop(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.get());
        addDrop(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.get());
        addDrop(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get());
        addDrop(AdornBlocks.COPPER_PIPE.get());
        addDrop(AdornBlocks.EXPOSED_COPPER_PIPE.get());
        addDrop(AdornBlocks.WEATHERED_COPPER_PIPE.get());
        addDrop(AdornBlocks.OXIDIZED_COPPER_PIPE.get());
        addDrop(AdornBlocks.WAXED_COPPER_PIPE.get());
        addDrop(AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get());
        addDrop(AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get());
        addDrop(AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get());
        addDrop(AdornBlocks.CANDLELIT_LANTERN.get());
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
