package juuxel.adorn.datagen

private typealias TemplateMaterialConfig = TemplateDsl.(Material) -> Unit

private val BASIC_SUBSTITUTION: TemplateMaterialConfig = { "id" with it.prefix }
private val EMPTY_SUBSTITUTION: TemplateMaterialConfig = {}

private operator fun TemplateMaterialConfig.plus(other: TemplateMaterialConfig): TemplateMaterialConfig = {
    this@plus(this, it)
    if (other != EMPTY_SUBSTITUTION) other(this, it)
}

internal class Generator<in M : Material>(
    val outputPathTemplate: String,
    val templatePath: String,
    val requiresCondition: Boolean = false,
    val substitutionConfig: TemplateMaterialConfig
) {
    companion object {
        val COMMON_GENERATORS: List<Generator<BuildingMaterial>> = listOf()
        val WOOD_GENERATORS: List<Generator<WoodMaterial>> = COMMON_GENERATORS + listOf(
            blockState("bench"),
            blockModel("bench_leg"),
            blockModel("bench_top"),
            itemModel("bench"),
            blockLootTable("bench"),
            recipe("bench"),
            recipeAdvancement("bench"),
        )
        val STONE_GENERATORS: List<Generator<StoneMaterial>> = COMMON_GENERATORS + listOf()
        val WOOL_GENERATORS: List<Generator<WoolMaterial>> = listOf(
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
        )

        private fun blockState(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "assets/adorn/blockstates/<mod-prefix><id.path>_$type.json",
                "block-states/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun blockModel(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "assets/adorn/models/block/<mod-prefix><id.path>_$type.json",
                "block-models/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun itemModel(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "assets/adorn/models/item/<mod-prefix><id.path>_$type.json",
                "item-models/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig
            )

        private fun blockLootTable(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "data/adorn/loot_tables/blocks/<mod-prefix><id.path>_$type.json",
                "loot-tables/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )

        private fun recipe(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "data/adorn/recipes/<mod-prefix><id.path>_$type.json",
                "recipes/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )

        private fun recipeAdvancement(type: String, substitutionConfig: TemplateMaterialConfig = EMPTY_SUBSTITUTION): Generator<Material> =
            Generator(
                "data/adorn/advancements/recipes/<mod-prefix><id.path>_$type.json",
                "recipe-advancements/$type.json",
                substitutionConfig = BASIC_SUBSTITUTION + substitutionConfig,
                requiresCondition = true
            )
    }
}
