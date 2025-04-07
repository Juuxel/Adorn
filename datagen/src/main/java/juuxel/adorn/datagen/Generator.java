package juuxel.adorn.datagen;

import java.util.ArrayList;
import java.util.List;

public record Generator(
    String id,
    String outputPathTemplate,
    String templatePath,
    boolean requiresCondition
) {
    public Generator(String id, String outputPathTemplate, String templatePath) {
        this(id, outputPathTemplate, templatePath, false);
    }

    public static final List<Generator> COMMON_GENERATORS = List.of(
        // Platforms
        blockState("platform"),
        itemAsset("platform"),
        blockLootTable("platform"),
        recipe("platform"),
        stonecuttingRecipe("platform"),
        recipeAdvancement("platform"),

        // Posts
        blockState("post"),
        itemAsset("post"),
        blockLootTable("post"),
        recipe("post"),
        stonecuttingRecipe("post"),
        recipeAdvancement("post"),

        // Steps
        blockState("step"),
        itemAsset("step"),
        blockLootTable("step"),
        recipe("step"),
        stonecuttingRecipe("step"),
        recipeAdvancement("step")
    );
    public static final List<Generator> UNSIDED_COMMON_GENERATORS = List.of(
        // Block models with sided variants
        blockModel("platform"),
        blockModel("post"),
        blockModel("step")
    );
    public static final List<Generator> WOOD_GENERATORS = concat(COMMON_GENERATORS, UNSIDED_COMMON_GENERATORS, List.of(
        // Benches
        blockState("bench"),
        blockModel("bench_leg"),
        blockModel("bench_top"),
        itemModel("bench"),
        itemAsset("bench"),
        blockLootTable("bench"),
        recipe("bench"),
        recipeAdvancement("bench"),

        // Chairs
        blockState("chair"),
        blockModel("chair_lower"),
        blockModel("chair_upper"),
        itemModel("chair"),
        itemAsset("chair"),
        blockLootTable("chair"),
        recipe("chair"),
        recipeAdvancement("chair"),

        // Coffee tables
        blockState("coffee_table"),
        blockModel("coffee_table"),
        itemAsset("coffee_table"),
        blockLootTable("coffee_table"),
        recipe("coffee_table"),
        recipeAdvancement("coffee_table"),

        // Drawers
        blockState("drawer"),
        blockModel("drawer"),
        itemAsset("drawer"),
        blockLootTable("drawer"),
        recipe("drawer"),
        recipeAdvancement("drawer"),

        // Kitchen counters
        blockState("kitchen_counter"),
        blockModel("kitchen_counter"),
        blockModel("kitchen_counter_connection_left"),
        blockModel("kitchen_counter_connection_right"),
        itemAsset("kitchen_counter"),
        blockLootTable("kitchen_counter"),
        recipe("kitchen_counter"),
        recipeAdvancement("kitchen_counter"),

        // Kitchen cupboard
        blockState("kitchen_cupboard"),
        blockModel("kitchen_cupboard_door"),
        itemModel("kitchen_cupboard"),
        itemAsset("kitchen_cupboard"),
        blockLootTable("kitchen_cupboard"),
        recipe("kitchen_cupboard"),
        recipeAdvancement("kitchen_cupboard"),

        // Kitchen sinks
        blockState("kitchen_sink"),
        blockModel("kitchen_sink"),
        itemAsset("kitchen_sink"),
        blockLootTable("kitchen_sink"),
        recipe("kitchen_sink"),
        recipeAdvancement("kitchen_sink"),

        // Shelves
        blockState("shelf"),
        blockModel("shelf"),
        itemAsset("shelf"),
        blockLootTable("shelf"),
        recipe("shelf"),
        recipeAdvancement("shelf"),

        // Table
        blockState("table"),
        blockModel("table"),
        blockModel("table_leg"),
        itemModel("table"),
        itemAsset("table"),
        blockLootTable("table"),
        recipe("table"),
        recipeAdvancement("table")
    ));
    public static final List<Generator> STONE_GENERATORS = COMMON_GENERATORS;
    public static final List<Generator> UNSIDED_STONE_GENERATORS = UNSIDED_COMMON_GENERATORS;
    public static final List<Generator> SIDED_STONE_GENERATORS = List.of(
        // Block models with sided variants
        blockModel("platform", "platform_with_sides"),
        blockModel("post", "post_with_sides"),
        blockModel("step", "step_with_sides")
    );
    public static final List<Generator> WOOL_GENERATORS = List.of(
        // Sofas
        blockState("sofa"),
        blockModel("sofa_arm_left"),
        blockModel("sofa_arm_right"),
        blockModel("sofa_center"),
        blockModel("sofa_corner_left"),
        blockModel("sofa_corner_right"),
        itemModel("sofa"),
        itemAsset("sofa"),
        blockLootTable("sofa"),
        recipe("sofa"),
        recipeAdvancement("sofa"),

        // Table lamps
        blockState("table_lamp"),
        blockModel("table_lamp"),
        itemAsset("table_lamp"),
        blockLootTable("table_lamp"),
        recipe("table_lamp"),
        recipeAdvancement("table_lamp"),

        // Candlelit lanterns
        blockModel("candlelit_lantern_hanging"),
        blockModel("candlelit_lantern_standing"),
        blockState("candlelit_lantern"),
        itemAsset("candlelit_lantern"),
        blockLootTable("candlelit_lantern"),
        recipe("candlelit_lantern"),
        recipeAdvancement("candlelit_lantern")
    );

    private static Generator blockState(String type) {
        return new Generator(
            "block_states/" + type,
            "assets/adorn/blockstates/<mod-prefix><id.path>_%s.json".formatted(type),
            "block-states/%s.json".formatted(type)
        );
    }

    private static Generator blockModel(String type) {
        return blockModel(type, type);
    }

    private static Generator blockModel(String type, String templateName) {
        return new Generator(
            "block_models/" + type,
            "assets/adorn/models/block/<mod-prefix><id.path>_%s.json".formatted(type),
            "block-models/%s.json".formatted(templateName)
        );
    }

    private static Generator itemModel(String type) {
        return new Generator(
            "item_models/" + type,
            "assets/adorn/models/item/<mod-prefix><id.path>_%s.json".formatted(type),
            "item-models/%s.json".formatted(type)
        );
    }

    private static Generator blockLootTable(String type) {
        return new Generator(
            "loot_tables/" + type,
            "data/adorn/loot_table/blocks/<mod-prefix><id.path>_%s.json".formatted(type),
            "loot-tables/%s.json".formatted(type),
            true
        );
    }

    private static Generator recipe(String type) {
        return new Generator(
            "recipes/" + type,
            "data/adorn/recipe/<mod-prefix><id.path>_%s.json".formatted(type),
            "recipes/%s.json".formatted(type),
            true
        );
    }

    private static Generator stonecuttingRecipe(String type) {
        return new Generator(
            "recipes/%s/stonecutting".formatted(type),
            "data/adorn/recipe/stonecutting/<mod-prefix><id.path>_%s.json".formatted(type),
            "recipes/stonecutting/%s.json".formatted(type),
            true
        );
    }

    private static Generator recipeAdvancement(String type) {
        return new Generator(
            "recipe_advancements/" + type,
            "data/adorn/advancement/recipes/<mod-prefix><id.path>_%s.json".formatted(type),
            "recipe-advancements/%s.json".formatted(type),
            true
        );
    }

    private static Generator itemAsset(String type) {
        return new Generator(
            "item_assets/" + type,
            "assets/adorn/items/<mod-prefix><id.path>_%s.json".formatted(type),
            "item_assets/%s.json".formatted(type),
            true
        );
    }

    @SafeVarargs
    private static List<Generator> concat(List<Generator>... lists) {
        List<Generator> result = new ArrayList<>();
        for (var list : lists) {
            result.addAll(list);
        }
        return result;
    }
}

