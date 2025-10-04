package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ConeVariantComponent;
import juuxel.adorn.data.util.ShapedRecipeJsonBuilderExtension;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidKey;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.recipe.BrewingRecipeJsonBuilder;
import juuxel.adorn.util.EntryOrTag;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AdornRecipeGenerator extends RecipeGenerator {
    private final RegistryEntryLookup<Item> itemLookup;
    private final ConditionApplier conditions;

    private AdornRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter, ConditionApplier conditions) {
        super(registries, exporter);
        this.itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
        this.conditions = conditions;
    }

    @Override
    public void generate() {
        generateChimneys();
        generateBooks();
        generateBrewing();
        generateCrates();
        generateMaterials();
        generatePaintedWood();
        generateCopperPipes();
        generateMiscDecorations();
        generateTools();
        generateCones();
        generateCautionSigns();
    }

    private void generateChimneys() {
        offerChimneyRecipe(exporter, AdornBlocks.BRICK_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.NORMAL_BRICKS), false);
        offerChimneyRecipe(exporter, AdornBlocks.BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.BRICKS), true, true);
        offerChimneyRecipe(exporter, AdornBlocks.COBBLESTONE_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.COBBLESTONES), true);
        offerChimneyRecipe(exporter, AdornBlocks.NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.NETHER_BRICKS), false);
        offerChimneyRecipe(exporter, AdornBlocks.NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.NETHER_BRICKS), true, true);
        offerChimneyRecipe(exporter, AdornBlocks.RED_NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.RED_NETHER_BRICKS), true);
        offerChimneyRecipe(exporter, AdornBlocks.STONE_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.STONE_BRICKS), true);
        offerChimneyRecipe(exporter, AdornBlocks.PRISMARINE_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.PRISMARINE_SHARD), false);
        offerModifiedPrismarineChimneyRecipe(exporter, AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.get(), Blocks.MAGMA_BLOCK);
        offerModifiedPrismarineChimneyRecipe(exporter, AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.get(), Blocks.SOUL_SAND);
    }

    private void generateBooks() {
        createShapeless(RecipeCategory.MISC, AdornItems.GUIDE_BOOK.get())
            .criterion("has_book", conditionsFromItem(Items.BOOK))
            .input(Items.BOOK)
            .input(ItemTags.WOOL)
            .input(ItemTags.WOOL)
            .offerTo(exporter);
        createShapeless(RecipeCategory.MISC, AdornItems.TRADERS_MANUAL.get())
            .criterion("has_book", conditionsFromItem(Items.BOOK))
            .input(Items.BOOK)
            .input(ConventionalItemTags.GOLD_INGOTS)
            .input(ConventionalItemTags.EMERALD_GEMS)
            .offerTo(exporter);
    }

    private void generateBrewing() {
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.BREWER.get())
            .criterion("has_mug", conditionsFromItem(AdornItems.MUG.get()))
            .pattern("II")
            .pattern("MI")
            .pattern("II")
            .input('I', ConventionalItemTags.IRON_INGOTS)
            .input('M', AdornItems.MUG.get())
            .offerTo(exporter);

        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.GLOW_BERRY_TEA.get())
            .first(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(exporter);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.HOT_CHOCOLATE.get())
            .first(ConventionalItemTags.COCOA_BEAN_CROPS)
            .second(MoreConventionalItemTags.MILK_FOODS)
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(exporter);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.HOT_CHOCOLATE.get())
            .first(ConventionalItemTags.COCOA_BEAN_CROPS)
            .fluid(new FluidIngredient(FluidKey.of(ConventionalFluidTags.MILK), 250, FluidUnit.LITRE))
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(
                conditions.apply(exporter, ResourceConditions.tagsPopulated(ConventionalFluidTags.MILK)),
                getItemPath(AdornItems.HOT_CHOCOLATE.get()) + "_from_fluid"
            );
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.NETHER_WART_COFFEE.get())
            .first(ConventionalItemTags.NETHER_WART_CROPS)
            .fluid(new FluidIngredient(FluidKey.of(Fluids.WATER), 250, FluidUnit.LITRE))
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(exporter);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.NETHER_WART_COFFEE.get())
            .first(ConventionalItemTags.NETHER_WART_CROPS)
            .second(Items.WATER_BUCKET)
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(exporter, convertBetween(AdornItems.NETHER_WART_COFFEE.get(), Items.WATER_BUCKET));
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.SWEET_BERRY_JUICE.get())
            .first(MoreConventionalItemTags.SWEET_BERRY_FOODS)
            .criterion("has_brewer", conditionsFromItem(AdornBlocks.BREWER.get()))
            .offerTo(exporter);
    }

    private void generateCrates() {
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.CRATE.get(), 4)
            .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
            .pattern("psp")
            .pattern("s s")
            .pattern("psp")
            .input('p', ItemTags.PLANKS)
            .input('s', ConventionalItemTags.WOODEN_RODS)
            .offerTo(exporter, "crates/crate");

        offerCrates(exporter, AdornBlocks.APPLE_CRATE.get(), Items.APPLE);
        offerCrates(exporter, AdornBlocks.BEETROOT_CRATE.get(), Items.BEETROOT);
        offerCrates(exporter, AdornBlocks.BEETROOT_SEED_CRATE.get(), Items.BEETROOT_SEEDS);
        offerCrates(exporter, AdornBlocks.CARROT_CRATE.get(), Items.CARROT);
        offerCrates(exporter, AdornBlocks.COCOA_BEAN_CRATE.get(), Items.COCOA_BEANS);
        offerCrates(exporter, AdornBlocks.EGG_CRATE.get(), Items.EGG);
        offerCrates(exporter, AdornBlocks.HONEYCOMB_CRATE.get(), Items.HONEYCOMB);
        offerCrates(exporter, AdornBlocks.MELON_CRATE.get(), Items.MELON_SLICE);
        offerCrates(exporter, AdornBlocks.MELON_SEED_CRATE.get(), Items.MELON_SEEDS);
        offerCrates(exporter, AdornBlocks.NETHER_WART_CRATE.get(), Items.NETHER_WART);
        offerCrates(exporter, AdornBlocks.POTATO_CRATE.get(), Items.POTATO);
        offerCrates(exporter, AdornBlocks.PUMPKIN_SEED_CRATE.get(), Items.PUMPKIN_SEEDS);
        offerCrates(exporter, AdornBlocks.SUGAR_CANE_CRATE.get(), Items.SUGAR_CANE);
        offerCrates(exporter, AdornBlocks.SWEET_BERRY_CRATE.get(), Items.SWEET_BERRIES);
        offerCrates(exporter, AdornBlocks.WHEAT_CRATE.get(), Items.WHEAT);
        offerCrates(exporter, AdornBlocks.WHEAT_SEED_CRATE.get(), Items.WHEAT_SEEDS);
    }

    private void generateMaterials() {
        createShaped(RecipeCategory.MISC, AdornItems.STONE_ROD.get(), 4)
            .criterion("has_stone", conditionsFromTag(ConventionalItemTags.STONES))
            .pattern("#")
            .pattern("#")
            .input('#', ConventionalItemTags.STONES)
            .offerTo(exporter);
        StonecuttingRecipeJsonBuilder.createStonecutting(
            Ingredient.ofTag(itemLookup.getOrThrow(ConventionalItemTags.STONES)),
            RecipeCategory.MISC,
            AdornItems.STONE_ROD.get(),
            2
        )
            .criterion("has_stone", conditionsFromTag(ConventionalItemTags.STONES))
            .offerTo(exporter, "stonecutting/" + getItemPath(AdornItems.STONE_ROD.get()));
        createShaped(RecipeCategory.MISC, AdornItems.MUG.get(), 3)
            .criterion("has_quartz", conditionsFromTag(ConventionalItemTags.QUARTZ_GEMS))
            .pattern("Q Q")
            .pattern(" Q ")
            .input('Q', ConventionalItemTags.QUARTZ_GEMS)
            .offerTo(exporter);
        createShapeless(RecipeCategory.MISC, AdornItems.COPPER_NUGGET.get(), 9)
            .criterion("has_copper_ingot", conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
            .input(ConventionalItemTags.COPPER_INGOTS)
            .offerTo(exporter);
        createShaped(RecipeCategory.MISC, Items.COPPER_INGOT)
            .criterion("has_copper_nugget", conditionsFromTag(MoreConventionalItemTags.COPPER_NUGGETS))
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .input('#', MoreConventionalItemTags.COPPER_NUGGETS)
            .offerTo(exporter, getItemPath(Items.COPPER_INGOT) + "_from_nuggets");
    }

    private void generatePaintedWood() {
        AdornBlocks.PAINTED_PLANKS.forEach((color, block) -> offerPlankDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.forEach((color, block) -> offerPaintedSlabRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.forEach((color, block) -> offerSlabDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.forEach((color, block) -> offerPaintedStairsRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.forEach((color, block) -> offerStairDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.forEach((color, block) -> offerPaintedFenceRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.forEach((color, block) -> offerFenceDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.forEach((color, block) -> offerPaintedFenceGateRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.forEach((color, block) -> offerFenceGateDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.forEach((color, block) -> offerPaintedPressurePlateRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.forEach((color, block) -> offerPressurePlateDyeingRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.forEach((color, block) -> offerPaintedButtonRecipe(exporter, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.forEach((color, block) -> offerButtonDyeingRecipe(exporter, block, color));

        for (DyeColor color : DyeColor.values()) {
            offerDyeingRecipe(exporter, color, AdornTags.CHAIRS.item(), BlockKind.CHAIR);
            offerDyeingRecipe(exporter, color, AdornTags.TABLES.item(), BlockKind.TABLE);
            offerDyeingRecipe(exporter, color, AdornTags.DRAWERS.item(), BlockKind.DRAWER);
            offerDyeingRecipe(exporter, color, AdornTags.KITCHEN_COUNTERS.item(), BlockKind.KITCHEN_COUNTER);
            offerDyeingRecipe(exporter, color, AdornTags.KITCHEN_CUPBOARDS.item(), BlockKind.KITCHEN_CUPBOARD);
            offerDyeingRecipe(exporter, color, AdornTags.KITCHEN_SINKS.item(), BlockKind.KITCHEN_SINK);
            offerDyeingRecipe(exporter, color, AdornTags.WOODEN_POSTS.item(), BlockKind.POST);
            offerDyeingRecipe(exporter, color, AdornTags.WOODEN_PLATFORMS.item(), BlockKind.PLATFORM);
            offerDyeingRecipe(exporter, color, AdornTags.WOODEN_STEPS.item(), BlockKind.STEP);
            offerDyeingRecipe(exporter, color, AdornTags.WOODEN_SHELVES.item(), BlockKind.SHELF);
            offerDyeingRecipe(exporter, color, AdornTags.COFFEE_TABLES.item(), BlockKind.COFFEE_TABLE);
            offerDyeingRecipe(exporter, color, AdornTags.BENCHES.item(), BlockKind.BENCH);
        }
    }

    private void generateCopperPipes() {
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.COPPER_PIPE.get(), 3)
            .criterion("has_copper_ingot", conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
            .pattern(".-.")
            .input('.', MoreConventionalItemTags.COPPER_NUGGETS)
            .input('-', ConventionalItemTags.COPPER_INGOTS)
            .offerTo(exporter);
        offerWaxingRecipe(exporter, AdornBlocks.WAXED_COPPER_PIPE.get(), AdornBlocks.COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(exporter, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get(), AdornBlocks.EXPOSED_COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(exporter, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get(), AdornBlocks.WEATHERED_COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(exporter, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get(), AdornBlocks.OXIDIZED_COPPER_PIPE.get(), "copper_pipes");
    }

    private void generateMiscDecorations() {
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.CANDLELIT_LANTERN.get())
            .criterion("has_candle", conditionsFromItem(Items.CANDLE))
            .group(AdornCommon.NAMESPACE + ":candlelit_lantern")
            .pattern("***")
            .pattern("*|*")
            .pattern("***")
            .input('*', ConventionalItemTags.IRON_NUGGETS)
            .input('|', Items.CANDLE)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.CHAIN_LINK_FENCE.get(), 4)
            .criterion("has_iron_ingot", conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
            .pattern(". .")
            .pattern(" - ")
            .pattern(". .")
            .input('.', ConventionalItemTags.IRON_NUGGETS)
            .input('-', ConventionalItemTags.IRON_INGOTS)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.PICKET_FENCE.get(), 3)
            .criterion("has_sticks", conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
            .pattern("|o|")
            .pattern("|||")
            .input('o', ConventionalItemTags.WHITE_DYES)
            .input('|', ConventionalItemTags.WOODEN_RODS)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.STONE_LADDER.get(), 3)
            .criterion("has_stone", conditionsFromTag(ConventionalItemTags.STONES))
            .pattern("/ /")
            .pattern("///")
            .pattern("/ /")
            .input('/', MoreConventionalItemTags.STONE_RODS)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, AdornItems.STONE_TORCH.get(), 4)
            .criterion("has_stone", conditionsFromTag(ConventionalItemTags.STONES))
            .pattern("C")
            .pattern("R")
            .input('C', ItemTags.COALS)
            .input('R', MoreConventionalItemTags.STONE_RODS)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get(), 3)
            .criterion("has_iron_ingot", conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
            .pattern("---")
            .pattern("/ /")
            .input('-', ConventionalItemTags.IRON_INGOTS)
            .input('/', MoreConventionalItemTags.STONE_RODS)
            .offerTo(exporter);
        createShapeless(RecipeCategory.DECORATIONS, AdornBlocks.TRADING_STATION.get(), 1)
            .criterion("has_emerald", conditionsFromTag(ConventionalItemTags.EMERALD_GEMS))
            .input(AdornTags.TABLES.item())
            .input(ConventionalItemTags.EMERALD_GEMS)
            .input(ConventionalItemTags.EMERALD_GEMS)
            .offerTo(exporter);
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.BARRICADE.get(), 4)
            .criterion("has_iron", conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
            .pattern("---")
            .pattern("| |")
            .pattern("| |")
            .input('-', ItemTags.PLANKS)
            .input('|', ConventionalItemTags.IRON_INGOTS)
            .offerTo(exporter);
    }

    private void generateTools() {
        createShaped(RecipeCategory.TOOLS, AdornItems.WATERING_CAN.get())
            .criterion("has_copper_ingot", conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
            .pattern(" I ")
            .pattern("IBI")
            .pattern(" II")
            .input('I', ConventionalItemTags.COPPER_INGOTS)
            .input('B', ConventionalItemTags.EMPTY_BUCKETS)
            .offerTo(exporter);
    }

    private void generateCones() {
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.WHITE, ConventionalItemTags.WHITE_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.ORANGE, ConventionalItemTags.ORANGE_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.MAGENTA, ConventionalItemTags.MAGENTA_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.YELLOW, ConventionalItemTags.YELLOW_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.LIME, ConventionalItemTags.LIME_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.PINK, ConventionalItemTags.PINK_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.GRAY, ConventionalItemTags.GRAY_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.CYAN, ConventionalItemTags.CYAN_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.PURPLE, ConventionalItemTags.PURPLE_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.BLUE, ConventionalItemTags.BLUE_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.BROWN, ConventionalItemTags.BROWN_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.GREEN, ConventionalItemTags.GREEN_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.RED, ConventionalItemTags.RED_DYES);
        offerWoodenConeRecipe(exporter, ConeVariant.Keys.BLACK, ConventionalItemTags.BLACK_DYES);
        offerStoneConeRecipe(exporter, ConeVariant.Keys.OBSIDIAN, ConventionalItemTags.NORMAL_OBSIDIANS);
    }

    private void generateCautionSigns() {
        createShaped(RecipeCategory.DECORATIONS, AdornBlocks.CAUTION_SIGN.get())
            .criterion("has_iron_ingot", conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
            .pattern(" I ")
            .pattern("IDI")
            .pattern(" I ")
            .input('I' ,ConventionalItemTags.IRON_INGOTS)
            .input('D' ,ConventionalItemTags.YELLOW_DYES)
            .offerTo(exporter);

        offerCautionSignRecipe(exporter, AdornBlocks.BEE_CAUTION_SIGN.get(), MoreConventionalItemTags.HONEYCOMBS);
        offerCautionSignRecipe(exporter, AdornBlocks.BOOK_CAUTION_SIGN.get(), MoreConventionalItemTags.BOOKS);
        offerCautionSignRecipe(exporter, AdornBlocks.CLIFF_CAUTION_SIGN.get(), ConventionalItemTags.COBBLESTONES);
        offerCautionSignRecipe(exporter, AdornBlocks.FORBIDDEN_CAUTION_SIGN.get(), ConventionalItemTags.FENCES);
        offerCautionSignRecipe(exporter, AdornBlocks.HELMET_CAUTION_SIGN.get(), ItemTags.HEAD_ARMOR);
        offerCautionSignRecipe(exporter, AdornBlocks.RAILS_CAUTION_SIGN.get(), ItemTags.RAILS);
        offerCautionSignRecipe(exporter, AdornBlocks.SURPRISE_CAUTION_SIGN.get(), ConventionalItemTags.EGGS);
    }

    private void offerChimneyRecipe(RecipeExporter exporter, ItemConvertible output, EntryOrTag<Item> ingredient, boolean fromBlock) {
        offerChimneyRecipe(exporter, output, ingredient, fromBlock, false);
    }

    private void offerChimneyRecipe(RecipeExporter exporter, ItemConvertible output, EntryOrTag<Item> ingredient, boolean fromBlock, boolean suffix) {
        var builder = createShaped(RecipeCategory.DECORATIONS, output, fromBlock ? 4 : 1)
            .criterion(has(ingredient), conditionsFrom(ingredient))
            .pattern(" # ")
            .pattern("#.#")
            .pattern(" # ")
            .input('.', ConventionalItemTags.IGNITER_TOOLS);
        switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> builder.input('#', item);
            case EntryOrTag.OfTag(var tag) -> builder.input('#', tag);
        }

        if (suffix) {
            builder.offerTo(exporter, getItemPath(output) + "_from_block");
        } else {
            builder.offerTo(exporter);
        }
    }

    private void offerModifiedPrismarineChimneyRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible ingredient) {
        createShaped(RecipeCategory.DECORATIONS, output, 1)
            .criterion(hasItem(ingredient), conditionsFromItem(ingredient))
            .pattern(" - ")
            .pattern("-#-")
            .pattern(" - ")
            .input('#', ingredient)
            .input('-', Items.PRISMARINE_SHARD)
            .offerTo(exporter);
    }

    private void offerCrates(RecipeExporter exporter, ItemConvertible crate, ItemConvertible contents) {
        offerCratePack(exporter, crate, contents);
        offerCrateUnpack(exporter, crate, contents);
    }

    private void offerCratePack(RecipeExporter exporter, ItemConvertible crate, ItemConvertible contents) {
        createShaped(RecipeCategory.DECORATIONS, crate)
            .criterion("has_crate", conditionsFromItem(AdornBlocks.CRATE.get()))
            .group(AdornCommon.NAMESPACE + ":pack_crate")
            .pattern("...")
            .pattern(".#.")
            .pattern("...")
            .input('.', contents)
            .input('#', AdornBlocks.CRATE.get())
            .offerTo(exporter, "crates/pack/" + getItemPath(contents));
    }

    private void offerCrateUnpack(RecipeExporter exporter, ItemConvertible crate, ItemConvertible contents) {
        createShapeless(RecipeCategory.DECORATIONS, contents, 8)
            .criterion(hasItem(crate), conditionsFromItem(crate))
            .group(AdornCommon.NAMESPACE + ":unpack_crate")
            .input(crate)
            .offerTo(exporter, "crates/unpack/" + getItemPath(contents));
    }

    private void offerPlankDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.PLANKS, "planks", false);
    }

    private void offerPaintedSlabRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.ofItems(planks))
            .group("wooden_slabs")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerSlabDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_SLABS, "slab", true);
    }

    private void offerPaintedStairsRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createStairsRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_stairs")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerStairDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_STAIRS, "stairs", true);
    }

    private void offerPaintedFenceRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createFenceRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_fence")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerFenceDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_FENCES, "fence", true);
    }

    private void offerPaintedFenceGateRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createFenceGateRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_fence")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerFenceGateDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.FENCE_GATES, "fence_gate", true);
    }

    private void offerPaintedPressurePlateRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createPressurePlateRecipe(RecipeCategory.REDSTONE, output, Ingredient.ofItems(planks))
            .group("wooden_pressure_plate")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerPressurePlateDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_PRESSURE_PLATES, "pressure_plate", true);
    }

    private void offerPaintedButtonRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        createButtonRecipe(output, Ingredient.ofItems(planks))
            .group("wooden_button")
            .criterion("has_planks", conditionsFromItem(planks))
            .offerTo(exporter);
    }

    private void offerButtonDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_BUTTONS, "button", true);
    }

    private void offerDyeingRecipe(RecipeExporter exporter, DyeColor color, TagKey<Item> ingredient, BlockKind kind) {
        var variant = BlockVariant.PAINTED_WOODS.get(color);
        var group = AdornCommon.NAMESPACE + ':' + kind.id();
        offerDyeingRecipe(exporter, BlockVariantSets.get(kind, variant).get(), color, ingredient, kind.id(), group, true);
    }

    private void offerDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color, TagKey<Item> ingredient, String kind, boolean suffix) {
        offerDyeingRecipe(exporter, output, color, ingredient, kind, "wooden_" + kind, suffix);
    }

    private void offerDyeingRecipe(RecipeExporter exporter, ItemConvertible output, DyeColor color, TagKey<Item> ingredient, String kind, String group, boolean suffix) {
        var builder = createShaped(RecipeCategory.BUILDING_BLOCKS, output, 8)
            .input('*', TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dyes/" + color.asString())))
            .input('#', ingredient)
            .pattern("###")
            .pattern("#*#")
            .pattern("###")
            .group(group)
            .criterion("has_" + kind, conditionsFromTag(ingredient));

        if (suffix) {
            builder.offerTo(exporter, getItemPath(output) + "_from_dyeing");
        } else {
            builder.offerTo(exporter);
        }
    }

    private void offerWaxingRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input, String group) {
        createShapeless(RecipeCategory.DECORATIONS, output)
            .criterion(hasItem(input), conditionsFromItem(input))
            .group(AdornCommon.NAMESPACE + ":waxed_" + group)
            .input(input)
            .input(MoreConventionalItemTags.HONEYCOMBS)
            .offerTo(exporter, "waxing/" + getItemPath(output));
    }

    private void offerWoodenConeRecipe(RecipeExporter exporter, RegistryKey<ConeVariant> variant, TagKey<Item> dyeTag) {
        var builder = createShaped(RecipeCategory.DECORATIONS, AdornItems.CONE.get())
            .criterion("has_slab", conditionsFromTag(ItemTags.WOODEN_SLABS))
            .group(AdornCommon.NAMESPACE + ":wooden_cones")
            .pattern("D")
            .pattern("|")
            .pattern("-")
            .input('D', dyeTag)
            .input('|', AdornTags.WOODEN_POSTS.item())
            .input('-', ItemTags.WOODEN_SLABS);
        ((ShapedRecipeJsonBuilderExtension) builder).adorn_setOutputModifier(stack -> {
            stack.set(AdornComponentTypes.CONE_VARIANT.get(), new ConeVariantComponent(variant));
            return stack;
        });
        builder.offerTo(exporter, variant.getValue().getPath() + "_cone");
    }

    private void offerStoneConeRecipe(RecipeExporter exporter, RegistryKey<ConeVariant> variant, TagKey<Item> input) {
        var builder = createShaped(RecipeCategory.DECORATIONS, AdornItems.CONE.get(), 4)
            .criterion(hasTag(input), conditionsFromTag(input))
            .pattern("|")
            .pattern("-")
            .input('|', input)
            .input('-', Items.SMOOTH_STONE_SLAB);
        ((ShapedRecipeJsonBuilderExtension) builder).adorn_setOutputModifier(stack -> {
            stack.set(AdornComponentTypes.CONE_VARIANT.get(), new ConeVariantComponent(variant));
            return stack;
        });
        builder.offerTo(exporter, variant.getValue().getPath() + "_cone");
    }

    private void offerCautionSignRecipe(RecipeExporter exporter, ItemConvertible output, TagKey<Item> ingredient) {
        createShapeless(RecipeCategory.DECORATIONS, output)
            .criterion(hasItem(AdornBlocks.CAUTION_SIGN.get()), conditionsFromItem(AdornBlocks.CAUTION_SIGN.get()))
            .group(AdornCommon.NAMESPACE + ":caution_signs")
            .input(AdornBlocks.CAUTION_SIGN.get())
            .input(ingredient)
            .offerTo(exporter);
    }

    private String has(EntryOrTag<Item> ingredient) {
        return switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> hasItem(item);
            case EntryOrTag.OfTag(var tag) -> hasTag(tag);
        };
    }

    private String hasTag(TagKey<Item> tag) {
        List<String> components = Arrays.asList(tag.id().getPath().split("/"));
        Collections.reverse(components);
        return "has_" + String.join("_", components);
    }

    private AdvancementCriterion<?> conditionsFrom(EntryOrTag<Item> ingredient) {
        return switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> conditionsFromItem(item);
            case EntryOrTag.OfTag(var tag) -> conditionsFromTag(tag);
        };
    }

    public static final class Provider extends FabricRecipeProvider {
        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            return new AdornRecipeGenerator(registries, exporter, this::withConditions);
        }

        @Override
        public String getName() {
            return "Adorn Recipes";
        }
    }

    @FunctionalInterface
    public interface ConditionApplier {
        RecipeExporter apply(RecipeExporter exporter, ResourceCondition... conditions);
    }
}
