package juuxel.adorn.datagen

data class GeneratorConfig(
    val woods: Set<MaterialEntry<WoodMaterial>>,
    val stones: Set<MaterialEntry<StoneMaterial>>,
    val wools: Set<MaterialEntry<ColorMaterial>>,
    val conditionType: ConditionType
) {
    data class MaterialEntry<out M : Material>(
        val material: M,
        val exclude: Set<String>,
        val replace: Map<String, String>
    )
}
