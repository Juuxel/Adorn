package juuxel.adorn.datagen

private typealias TemplateMaterialConfig = TemplateDsl.(Material) -> Unit

private val BASIC_SUBSTITUTION: TemplateMaterialConfig = { "id" with it.prefix }
private val EMPTY_SUBSTITUTION: TemplateMaterialConfig = {}

private operator fun TemplateMaterialConfig.plus(other: TemplateMaterialConfig): TemplateMaterialConfig = {
    this@plus(this, it)
    if (other != EMPTY_SUBSTITUTION) other(this, it)
}

internal class Generator<in M : Material>(
    val id: String,
    val outputPathTemplate: String,
    val templatePath: String,
    val requiresCondition: Boolean = false,
    val substitutionConfig: TemplateMaterialConfig
) {
    companion object {
        val COMMON_GENERATORS: List<Generator<BuildingMaterial>> = listOf()
        val WOOD_GENERATORS: List<Generator<WoodMaterial>> = COMMON_GENERATORS + listOf(
            // Benches
            blockState("bench"),
            blockModel("bench_leg"),
            blockModel("bench_top"),
            itemModel("bench"),
            blockLootTable("bench"),
            recipe("bench"),
            recipeAdvancement("bench"),

            // Chairs
            blockState("chair"),
            blockModel("chair_lower"),
            blockModel("chair_upper"),
            itemModel("chair"),
            blockLootTable("chair"),
            recipe("chair"),
            recipeAdvancement("chair"),

            // Coffee tables
            blockState("coffee_table"),
            blockModel("coffee_table"),
            itemModel("coffee_table"),
            blockLootTable("coffee_table"),
            recipe("coffee_table"),
            recipeAdvancement("coffee_table"),

            // Drawers
            blockState("drawer"),
            blockModel("drawer"),
            itemModel("drawer"),
            blockLootTable("drawer"),
            recipe("drawer"),
            recipeAdvancement("drawer"),
        )
        val STONE_GENERATORS: List<Generator<StoneMaterial>> = COMMON_GENERATORS + listOf()
        val WOOL_GENERATORS: List<Generator<WoolMaterial>> = listOf(
            // Sofas
            blockState("sofa"),
            blockModel("sofa_arm_left"),
            blockModel("sofa_arm_right"),
            blockModel("sofa_center"),
            blockModel("sofa_corner_left"),
            blockModel("sofa_corner_right"),
            itemModel("sofa"),
            blockLootTable("sofa"),
            recipe("sofa"),
            recipeAdvancement("sofa"),

            // Table lamps
            blockState("table_lamp"),
            blockModel("table_lamp"),
            itemModel("table_lamp"),
            blockLootTable("table_lamp"),
            recipe("table_lamp"),
            recipeAdvancement("table_lamp"),
        )

        private fun blockState(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "block_states/$type",
                "assets/adorn/blockstates/<mod-prefix><id.path>_$type.json",
                "block-states/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun blockModel(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "block_models/$type",
                "assets/adorn/models/block/<mod-prefix><id.path>_$type.json",
                "block-models/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun itemModel(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "item_models/$type",
                "assets/adorn/models/item/<mod-prefix><id.path>_$type.json",
                "item-models/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun blockLootTable(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "loot_tables/$type",
                "data/adorn/loot_tables/blocks/<mod-prefix><id.path>_$type.json",
                "loot-tables/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )

        private fun recipe(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "recipes/$type",
                "data/adorn/recipes/<mod-prefix><id.path>_$type.json",
                "recipes/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )

        private fun recipeAdvancement(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "recipe_advancements/$type",
                "data/adorn/advancements/recipes/<mod-prefix><id.path>_$type.json",
                "recipe-advancements/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )
    }
}
