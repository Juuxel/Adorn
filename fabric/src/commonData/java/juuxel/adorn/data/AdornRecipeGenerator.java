package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.data.util.ShapedRecipeJsonBuilderExtension;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidKey;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.recipe.BrewingRecipeJsonBuilder;
import juuxel.adorn.util.Dyes;
import juuxel.adorn.util.EntryOrTag;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AdornRecipeGenerator extends RecipeProvider {
    private final HolderGetter<Item> itemLookup;
    private final ConditionApplier conditions;

    private AdornRecipeGenerator(HolderLookup.Provider registries, RecipeOutput exporter, ConditionApplier conditions) {
        super(registries, exporter);
        this.itemLookup = registries.lookupOrThrow(Registries.ITEM);
        this.conditions = conditions;
    }

    @Override
    public void buildRecipes() {
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
        offerChimneyRecipe(output, AdornBlocks.BRICK_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.NORMAL_BRICKS), false);
        offerChimneyRecipe(output, AdornBlocks.BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.BRICKS), true, true);
        offerChimneyRecipe(output, AdornBlocks.COBBLESTONE_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.COBBLESTONES), true);
        offerChimneyRecipe(output, AdornBlocks.NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfTag<>(ConventionalItemTags.NETHER_BRICKS), false);
        offerChimneyRecipe(output, AdornBlocks.NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.NETHER_BRICKS), true, true);
        offerChimneyRecipe(output, AdornBlocks.RED_NETHER_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.RED_NETHER_BRICKS), true);
        offerChimneyRecipe(output, AdornBlocks.STONE_BRICK_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.STONE_BRICKS), true);
        offerChimneyRecipe(output, AdornBlocks.PRISMARINE_CHIMNEY.get(), new EntryOrTag.OfEntry<>(Items.PRISMARINE_SHARD), false);
        offerModifiedPrismarineChimneyRecipe(output, AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.get(), Blocks.MAGMA_BLOCK);
        offerModifiedPrismarineChimneyRecipe(output, AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.get(), Blocks.SOUL_SAND);
    }

    private void generateBooks() {
        shapeless(RecipeCategory.MISC, AdornItems.GUIDE_BOOK.get())
            .unlockedBy("has_book", has(Items.BOOK))
            .requires(Items.BOOK)
            .requires(ItemTags.WOOL)
            .requires(ItemTags.WOOL)
            .save(output);
        shapeless(RecipeCategory.MISC, AdornItems.TRADERS_MANUAL.get())
            .unlockedBy("has_book", has(Items.BOOK))
            .requires(Items.BOOK)
            .requires(ConventionalItemTags.GOLD_INGOTS)
            .requires(ConventionalItemTags.EMERALD_GEMS)
            .save(output);
    }

    private void generateBrewing() {
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.BREWER.get())
            .unlockedBy("has_mug", has(AdornItems.MUG.get()))
            .pattern("II")
            .pattern("MI")
            .pattern("II")
            .define('I', ConventionalItemTags.IRON_INGOTS)
            .define('M', AdornItems.MUG.get())
            .save(output);

        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.GLOW_BERRY_TEA.get())
            .first(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(output);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.HOT_CHOCOLATE.get())
            .first(ConventionalItemTags.COCOA_BEAN_CROPS)
            .second(MoreConventionalItemTags.MILK_FOODS)
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(output);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.HOT_CHOCOLATE.get())
            .first(ConventionalItemTags.COCOA_BEAN_CROPS)
            .fluid(new FluidIngredient(FluidKey.of(ConventionalFluidTags.MILK), 250, FluidUnit.LITRE))
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(
                conditions.apply(output, ResourceConditions.tagsPopulated(ConventionalFluidTags.MILK)),
                getItemName(AdornItems.HOT_CHOCOLATE.get()) + "_from_fluid"
            );
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.NETHER_WART_COFFEE.get())
            .first(ConventionalItemTags.NETHER_WART_CROPS)
            .fluid(new FluidIngredient(FluidKey.of(Fluids.WATER), 250, FluidUnit.LITRE))
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(output);
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.NETHER_WART_COFFEE.get())
            .first(ConventionalItemTags.NETHER_WART_CROPS)
            .second(Items.WATER_BUCKET)
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(output, getConversionRecipeName(AdornItems.NETHER_WART_COFFEE.get(), Items.WATER_BUCKET));
        BrewingRecipeJsonBuilder.create(itemLookup, AdornItems.SWEET_BERRY_JUICE.get())
            .first(MoreConventionalItemTags.SWEET_BERRY_FOODS)
            .criterion("has_brewer", has(AdornBlocks.BREWER.get()))
            .offerTo(output);
    }

    private void generateCrates() {
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.CRATE.get(), 4)
            .unlockedBy("has_planks", has(ItemTags.PLANKS))
            .pattern("psp")
            .pattern("s s")
            .pattern("psp")
            .define('p', ItemTags.PLANKS)
            .define('s', ConventionalItemTags.WOODEN_RODS)
            .save(output, "crates/crate");

        offerCrates(output, AdornBlocks.APPLE_CRATE.get(), Items.APPLE);
        offerCrates(output, AdornBlocks.BEETROOT_CRATE.get(), Items.BEETROOT);
        offerCrates(output, AdornBlocks.BEETROOT_SEED_CRATE.get(), Items.BEETROOT_SEEDS);
        offerCrates(output, AdornBlocks.CARROT_CRATE.get(), Items.CARROT);
        offerCrates(output, AdornBlocks.COCOA_BEAN_CRATE.get(), Items.COCOA_BEANS);
        offerCrates(output, AdornBlocks.EGG_CRATE.get(), Items.EGG);
        offerCrates(output, AdornBlocks.HONEYCOMB_CRATE.get(), Items.HONEYCOMB);
        offerCrates(output, AdornBlocks.MELON_CRATE.get(), Items.MELON_SLICE);
        offerCrates(output, AdornBlocks.MELON_SEED_CRATE.get(), Items.MELON_SEEDS);
        offerCrates(output, AdornBlocks.NETHER_WART_CRATE.get(), Items.NETHER_WART);
        offerCrates(output, AdornBlocks.POTATO_CRATE.get(), Items.POTATO);
        offerCrates(output, AdornBlocks.PUMPKIN_SEED_CRATE.get(), Items.PUMPKIN_SEEDS);
        offerCrates(output, AdornBlocks.SUGAR_CANE_CRATE.get(), Items.SUGAR_CANE);
        offerCrates(output, AdornBlocks.SWEET_BERRY_CRATE.get(), Items.SWEET_BERRIES);
        offerCrates(output, AdornBlocks.WHEAT_CRATE.get(), Items.WHEAT);
        offerCrates(output, AdornBlocks.WHEAT_SEED_CRATE.get(), Items.WHEAT_SEEDS);
    }

    private void generateMaterials() {
        shaped(RecipeCategory.MISC, AdornItems.STONE_ROD.get(), 4)
            .unlockedBy("has_stone", has(ConventionalItemTags.STONES))
            .pattern("#")
            .pattern("#")
            .define('#', ConventionalItemTags.STONES)
            .save(output);
        SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(itemLookup.getOrThrow(ConventionalItemTags.STONES)),
                RecipeCategory.MISC,
                AdornItems.STONE_ROD.get(),
                2
            )
            .unlockedBy("has_stone", has(ConventionalItemTags.STONES))
            .save(output, "stonecutting/" + getItemName(AdornItems.STONE_ROD.get()));
        shaped(RecipeCategory.MISC, AdornItems.MUG.get(), 3)
            .unlockedBy("has_quartz", has(ConventionalItemTags.QUARTZ_GEMS))
            .pattern("Q Q")
            .pattern(" Q ")
            .define('Q', ConventionalItemTags.QUARTZ_GEMS)
            .save(output);
        shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
            .unlockedBy("has_copper_nugget", has(MoreConventionalItemTags.COPPER_NUGGETS))
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', MoreConventionalItemTags.COPPER_NUGGETS)
            .save(output, getItemName(Items.COPPER_INGOT) + "_from_nuggets");
    }

    private void generatePaintedWood() {
        AdornBlocks.PAINTED_PLANKS.forEach((color, block) -> offerPlankDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.forEach((color, block) -> offerPaintedSlabRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_SLABS.forEach((color, block) -> offerSlabDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.forEach((color, block) -> offerPaintedStairsRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_STAIRS.forEach((color, block) -> offerStairDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.forEach((color, block) -> offerPaintedFenceRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_FENCES.forEach((color, block) -> offerFenceDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.forEach((color, block) -> offerPaintedFenceGateRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.forEach((color, block) -> offerFenceGateDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.forEach((color, block) -> offerPaintedPressurePlateRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.forEach((color, block) -> offerPressurePlateDyeingRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.forEach((color, block) -> offerPaintedButtonRecipe(output, block, color));
        AdornBlocks.PAINTED_WOOD_BUTTONS.forEach((color, block) -> offerButtonDyeingRecipe(output, block, color));

        for (DyeColor color : Dyes.ALL_DYES) {
            offerDyeingRecipe(output, color, AdornTags.CHAIRS.item(), BlockKind.CHAIR);
            offerDyeingRecipe(output, color, AdornTags.TABLES.item(), BlockKind.TABLE);
            offerDyeingRecipe(output, color, AdornTags.DRAWERS.item(), BlockKind.DRAWER);
            offerDyeingRecipe(output, color, AdornTags.KITCHEN_COUNTERS.item(), BlockKind.KITCHEN_COUNTER);
            offerDyeingRecipe(output, color, AdornTags.KITCHEN_CUPBOARDS.item(), BlockKind.KITCHEN_CUPBOARD);
            offerDyeingRecipe(output, color, AdornTags.KITCHEN_SINKS.item(), BlockKind.KITCHEN_SINK);
            offerDyeingRecipe(output, color, AdornTags.WOODEN_POSTS.item(), BlockKind.POST);
            offerDyeingRecipe(output, color, AdornTags.WOODEN_PLATFORMS.item(), BlockKind.PLATFORM);
            offerDyeingRecipe(output, color, AdornTags.WOODEN_STEPS.item(), BlockKind.STEP);
            offerDyeingRecipe(output, color, AdornTags.WOODEN_SHELVES.item(), BlockKind.SHELF);
            offerDyeingRecipe(output, color, AdornTags.COFFEE_TABLES.item(), BlockKind.COFFEE_TABLE);
            offerDyeingRecipe(output, color, AdornTags.BENCHES.item(), BlockKind.BENCH);
        }
    }

    private void generateCopperPipes() {
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.COPPER_PIPE.get(), 3)
            .unlockedBy("has_copper_ingot", has(ConventionalItemTags.COPPER_INGOTS))
            .pattern(".-.")
            .define('.', MoreConventionalItemTags.COPPER_NUGGETS)
            .define('-', ConventionalItemTags.COPPER_INGOTS)
            .save(output);
        offerWaxingRecipe(output, AdornBlocks.WAXED_COPPER_PIPE.get(), AdornBlocks.COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(output, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get(), AdornBlocks.EXPOSED_COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(output, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get(), AdornBlocks.WEATHERED_COPPER_PIPE.get(), "copper_pipes");
        offerWaxingRecipe(output, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get(), AdornBlocks.OXIDIZED_COPPER_PIPE.get(), "copper_pipes");
    }

    private void generateMiscDecorations() {
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.CANDLELIT_LANTERN.get())
            .unlockedBy("has_candle", has(Items.CANDLE))
            .group(AdornCommon.NAMESPACE + ":candlelit_lantern")
            .pattern("***")
            .pattern("*|*")
            .pattern("***")
            .define('*', ConventionalItemTags.IRON_NUGGETS)
            .define('|', Items.CANDLE)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.CHAIN_LINK_FENCE.get(), 4)
            .unlockedBy("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
            .pattern(". .")
            .pattern(" - ")
            .pattern(". .")
            .define('.', ConventionalItemTags.IRON_NUGGETS)
            .define('-', ConventionalItemTags.IRON_INGOTS)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.PICKET_FENCE.get(), 3)
            .unlockedBy("has_sticks", has(ConventionalItemTags.WOODEN_RODS))
            .pattern("|o|")
            .pattern("|||")
            .define('o', ConventionalItemTags.WHITE_DYES)
            .define('|', ConventionalItemTags.WOODEN_RODS)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.STONE_LADDER.get(), 3)
            .unlockedBy("has_stone", has(ConventionalItemTags.STONES))
            .pattern("/ /")
            .pattern("///")
            .pattern("/ /")
            .define('/', MoreConventionalItemTags.STONE_RODS)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, AdornItems.STONE_TORCH.get(), 4)
            .unlockedBy("has_stone", has(ConventionalItemTags.STONES))
            .pattern("C")
            .pattern("R")
            .define('C', ItemTags.COALS)
            .define('R', MoreConventionalItemTags.STONE_RODS)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get(), 3)
            .unlockedBy("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
            .pattern("---")
            .pattern("/ /")
            .define('-', ConventionalItemTags.IRON_INGOTS)
            .define('/', MoreConventionalItemTags.STONE_RODS)
            .save(output);
        shapeless(RecipeCategory.DECORATIONS, AdornBlocks.TRADING_STATION.get(), 1)
            .unlockedBy("has_emerald", has(ConventionalItemTags.EMERALD_GEMS))
            .requires(AdornTags.TABLES.item())
            .requires(ConventionalItemTags.EMERALD_GEMS)
            .requires(ConventionalItemTags.EMERALD_GEMS)
            .save(output);
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.BARRICADE.get(), 4)
            .unlockedBy("has_iron", has(ConventionalItemTags.IRON_INGOTS))
            .pattern("---")
            .pattern("| |")
            .pattern("| |")
            .define('-', ItemTags.PLANKS)
            .define('|', ConventionalItemTags.IRON_INGOTS)
            .save(output);
    }

    private void generateTools() {
        shaped(RecipeCategory.TOOLS, AdornItems.WATERING_CAN.get())
            .unlockedBy("has_copper_ingot", has(ConventionalItemTags.COPPER_INGOTS))
            .pattern(" I ")
            .pattern("IBI")
            .pattern(" II")
            .define('I', ConventionalItemTags.COPPER_INGOTS)
            .define('B', ConventionalItemTags.EMPTY_BUCKETS)
            .save(output);
    }

    private void generateCones() {
        offerWoodenConeRecipe(output, ConeVariant.Keys.WHITE, ConventionalItemTags.WHITE_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.ORANGE, ConventionalItemTags.ORANGE_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.MAGENTA, ConventionalItemTags.MAGENTA_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.YELLOW, ConventionalItemTags.YELLOW_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.LIME, ConventionalItemTags.LIME_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.PINK, ConventionalItemTags.PINK_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.GRAY, ConventionalItemTags.GRAY_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.CYAN, ConventionalItemTags.CYAN_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.PURPLE, ConventionalItemTags.PURPLE_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.BLUE, ConventionalItemTags.BLUE_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.BROWN, ConventionalItemTags.BROWN_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.GREEN, ConventionalItemTags.GREEN_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.RED, ConventionalItemTags.RED_DYES);
        offerWoodenConeRecipe(output, ConeVariant.Keys.BLACK, ConventionalItemTags.BLACK_DYES);
        offerStoneConeRecipe(output, ConeVariant.Keys.OBSIDIAN, ConventionalItemTags.NORMAL_OBSIDIANS);
    }

    private void generateCautionSigns() {
        shaped(RecipeCategory.DECORATIONS, AdornBlocks.CAUTION_SIGN.get())
            .unlockedBy("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
            .pattern(" I ")
            .pattern("IDI")
            .pattern(" I ")
            .define('I', ConventionalItemTags.IRON_INGOTS)
            .define('D', ConventionalItemTags.YELLOW_DYES)
            .save(output);

        offerCautionSignRecipe(output, AdornBlocks.BEE_CAUTION_SIGN.get(), MoreConventionalItemTags.HONEYCOMBS);
        offerCautionSignRecipe(output, AdornBlocks.BOOK_CAUTION_SIGN.get(), MoreConventionalItemTags.BOOKS);
        offerCautionSignRecipe(output, AdornBlocks.CLIFF_CAUTION_SIGN.get(), ConventionalItemTags.COBBLESTONES);
        offerCautionSignRecipe(output, AdornBlocks.FORBIDDEN_CAUTION_SIGN.get(), ConventionalItemTags.FENCES);
        offerCautionSignRecipe(output, AdornBlocks.HELMET_CAUTION_SIGN.get(), ItemTags.HEAD_ARMOR);
        offerCautionSignRecipe(output, AdornBlocks.RAILS_CAUTION_SIGN.get(), ItemTags.RAILS);
        offerCautionSignRecipe(output, AdornBlocks.SURPRISE_CAUTION_SIGN.get(), ConventionalItemTags.EGGS);
    }

    private void offerChimneyRecipe(RecipeOutput exporter, ItemLike output, EntryOrTag<Item> ingredient, boolean fromBlock) {
        offerChimneyRecipe(exporter, output, ingredient, fromBlock, false);
    }

    private void offerChimneyRecipe(RecipeOutput exporter, ItemLike output, EntryOrTag<Item> ingredient, boolean fromBlock, boolean suffix) {
        var builder = shaped(RecipeCategory.DECORATIONS, output, fromBlock ? 4 : 1)
            .unlockedBy(has(ingredient), conditionsFrom(ingredient))
            .pattern(" # ")
            .pattern("#.#")
            .pattern(" # ")
            .define('.', ConventionalItemTags.IGNITER_TOOLS);
        switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> builder.define('#', item);
            case EntryOrTag.OfTag(var tag) -> builder.define('#', tag);
        }

        if (suffix) {
            builder.save(exporter, getItemName(output) + "_from_block");
        } else {
            builder.save(exporter);
        }
    }

    private void offerModifiedPrismarineChimneyRecipe(RecipeOutput exporter, ItemLike output, ItemLike ingredient) {
        shaped(RecipeCategory.DECORATIONS, output, 1)
            .unlockedBy(getHasName(ingredient), has(ingredient))
            .pattern(" - ")
            .pattern("-#-")
            .pattern(" - ")
            .define('#', ingredient)
            .define('-', Items.PRISMARINE_SHARD)
            .save(exporter);
    }

    private void offerCrates(RecipeOutput exporter, ItemLike crate, ItemLike contents) {
        offerCratePack(exporter, crate, contents);
        offerCrateUnpack(exporter, crate, contents);
    }

    private void offerCratePack(RecipeOutput exporter, ItemLike crate, ItemLike contents) {
        shaped(RecipeCategory.DECORATIONS, crate)
            .unlockedBy("has_crate", has(AdornBlocks.CRATE.get()))
            .group(AdornCommon.NAMESPACE + ":pack_crate")
            .pattern("...")
            .pattern(".#.")
            .pattern("...")
            .define('.', contents)
            .define('#', AdornBlocks.CRATE.get())
            .save(exporter, "crates/pack/" + getItemName(contents));
    }

    private void offerCrateUnpack(RecipeOutput exporter, ItemLike crate, ItemLike contents) {
        shapeless(RecipeCategory.DECORATIONS, contents, 8)
            .unlockedBy(getHasName(crate), has(crate))
            .group(AdornCommon.NAMESPACE + ":unpack_crate")
            .requires(crate)
            .save(exporter, "crates/unpack/" + getItemName(contents));
    }

    private void offerPlankDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.PLANKS, "planks", false);
    }

    private void offerPaintedSlabRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, output, Ingredient.of(planks))
            .group("wooden_slabs")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerSlabDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_SLABS, "slab", true);
    }

    private void offerPaintedStairsRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        stairBuilder(output, Ingredient.of(planks))
            .group("wooden_stairs")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerStairDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_STAIRS, "stairs", true);
    }

    private void offerPaintedFenceRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        fenceBuilder(output, Ingredient.of(planks))
            .group("wooden_fence")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerFenceDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_FENCES, "fence", true);
    }

    private void offerPaintedFenceGateRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        fenceGateBuilder(output, Ingredient.of(planks))
            .group("wooden_fence")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerFenceGateDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.FENCE_GATES, "fence_gate", true);
    }

    private void offerPaintedPressurePlateRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        pressurePlateBuilder(RecipeCategory.REDSTONE, output, Ingredient.of(planks))
            .group("wooden_pressure_plate")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerPressurePlateDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_PRESSURE_PLATES, "pressure_plate", true);
    }

    private void offerPaintedButtonRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        var planks = AdornBlocks.PAINTED_PLANKS.getEager(color);
        buttonBuilder(output, Ingredient.of(planks))
            .group("wooden_button")
            .unlockedBy("has_planks", has(planks))
            .save(exporter);
    }

    private void offerButtonDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color) {
        offerDyeingRecipe(exporter, output, color, ItemTags.WOODEN_BUTTONS, "button", true);
    }

    private void offerDyeingRecipe(RecipeOutput exporter, DyeColor color, TagKey<Item> ingredient, BlockKind kind) {
        var variant = BlockVariant.PAINTED_WOODS.get(color);
        var group = AdornCommon.NAMESPACE + ':' + kind.id();
        offerDyeingRecipe(exporter, BlockVariantSets.get(kind, variant).get(), color, ingredient, kind.id(), group, true);
    }

    private void offerDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color, TagKey<Item> ingredient, String kind, boolean suffix) {
        offerDyeingRecipe(exporter, output, color, ingredient, kind, "wooden_" + kind, suffix);
    }

    private void offerDyeingRecipe(RecipeOutput exporter, ItemLike output, DyeColor color, TagKey<Item> ingredient, String kind, String group, boolean suffix) {
        var builder = shaped(RecipeCategory.BUILDING_BLOCKS, output, 8)
            .define('*', TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "dyes/" + color.getSerializedName())))
            .define('#', ingredient)
            .pattern("###")
            .pattern("#*#")
            .pattern("###")
            .group(group)
            .unlockedBy("has_" + kind, has(ingredient));

        if (suffix) {
            builder.save(exporter, getItemName(output) + "_from_dyeing");
        } else {
            builder.save(exporter);
        }
    }

    private void offerWaxingRecipe(RecipeOutput exporter, ItemLike output, ItemLike input, String group) {
        shapeless(RecipeCategory.DECORATIONS, output)
            .unlockedBy(getHasName(input), has(input))
            .group(AdornCommon.NAMESPACE + ":waxed_" + group)
            .requires(input)
            .requires(MoreConventionalItemTags.HONEYCOMBS)
            .save(exporter, "waxing/" + getItemName(output));
    }

    private void offerWoodenConeRecipe(RecipeOutput exporter, ResourceKey<ConeVariant> variant, TagKey<Item> dyeTag) {
        var builder = shaped(RecipeCategory.DECORATIONS, AdornItems.CONE.get())
            .unlockedBy("has_slab", has(ItemTags.WOODEN_SLABS))
            .group(AdornCommon.NAMESPACE + ":wooden_cones")
            .pattern("D")
            .pattern("|")
            .pattern("-")
            .define('D', dyeTag)
            .define('|', AdornTags.WOODEN_POSTS.item())
            .define('-', ItemTags.WOODEN_SLABS);
        ((ShapedRecipeJsonBuilderExtension) builder).adorn_setOutputModifier(template -> setComponentInTemplate(
            template,
            AdornComponentTypes.CONE_VARIANT.get(),
            registries.getOrThrow(variant)
        ));
        builder.save(exporter, variant.identifier().getPath() + "_cone");
    }

    private void offerStoneConeRecipe(RecipeOutput exporter, ResourceKey<ConeVariant> variant, TagKey<Item> input) {
        var builder = shaped(RecipeCategory.DECORATIONS, AdornItems.CONE.get(), 4)
            .unlockedBy(hasTag(input), has(input))
            .pattern("|")
            .pattern("-")
            .define('|', input)
            .define('-', Items.SMOOTH_STONE_SLAB);
        ((ShapedRecipeJsonBuilderExtension) builder).adorn_setOutputModifier(template -> setComponentInTemplate(
            template,
            AdornComponentTypes.CONE_VARIANT.get(),
            registries.getOrThrow(variant)
        ));
        builder.save(exporter, variant.identifier().getPath() + "_cone");
    }

    private void offerCautionSignRecipe(RecipeOutput exporter, ItemLike output, TagKey<Item> ingredient) {
        shapeless(RecipeCategory.DECORATIONS, output)
            .unlockedBy(getHasName(AdornBlocks.CAUTION_SIGN.get()), has(AdornBlocks.CAUTION_SIGN.get()))
            .group(AdornCommon.NAMESPACE + ":caution_signs")
            .requires(AdornBlocks.CAUTION_SIGN.get())
            .requires(ingredient)
            .save(exporter);
    }

    private String has(EntryOrTag<Item> ingredient) {
        return switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> getHasName(item);
            case EntryOrTag.OfTag(var tag) -> hasTag(tag);
        };
    }

    private String hasTag(TagKey<Item> tag) {
        List<String> components = Arrays.asList(tag.location().getPath().split("/"));
        Collections.reverse(components);
        return "has_" + String.join("_", components);
    }

    private Criterion<?> conditionsFrom(EntryOrTag<Item> ingredient) {
        return switch (ingredient) {
            case EntryOrTag.OfEntry(var item) -> has(item);
            case EntryOrTag.OfTag(var tag) -> has(tag);
        };
    }

    private static <T> ItemStackTemplate setComponentInTemplate(ItemStackTemplate template, DataComponentType<T> type, T value) {
        var existingComponents = template.components().entrySet()
            .stream()
            .<TypedDataComponent<?>>map(entry -> TypedDataComponent.createUnchecked(entry.getKey(), entry.getValue()))
            .toList();

        var newPatch = DataComponentPatch.builder()
            .set(existingComponents)
            .set(type, value)
            .build();

        return new ItemStackTemplate(template.item(), template.count(), newPatch);
    }

    public static final class Provider extends FabricRecipeProvider {
        public Provider(FabricPackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
            return new AdornRecipeGenerator(registries, exporter, this::withConditions);
        }

        @Override
        public String getName() {
            return "Adorn Recipes";
        }
    }

    @FunctionalInterface
    public interface ConditionApplier {
        RecipeOutput apply(RecipeOutput exporter, ResourceCondition... conditions);
    }
}
