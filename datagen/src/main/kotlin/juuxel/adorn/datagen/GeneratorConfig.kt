package juuxel.adorn.datagen

data class GeneratorConfig(
    val woods: Set<MaterialEntry<WoodMaterial>>,
    val stones: Set<MaterialEntry<StoneMaterial>>,
    val wools: Set<MaterialEntry<WoolMaterial>>,
    val genericMaterials: Set<MaterialEntry<GenericMaterial>>,
    val conditionType: ConditionType,
    val rootReplacements: Map<String, String>,
) {
    data class MaterialEntry<out M : Material>(
        val material: M,
        val exclude: Set<String>,
        val replace: Map<String, String>
    )
}
