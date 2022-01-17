package juuxel.adorn.datagen

enum class ConditionType(
    val separateFilePathTemplate: String? = null,
    val separateFileTemplatePath: String? = null,
    val conditionsInFileTemplatePathsByType: Map<String, String> = emptyMap()
) {
    NONE,
    FABRIC(conditionsInFileTemplatePathsByType = mapOf("load" to "fabric-conditions.json")),
    FORGE(conditionsInFileTemplatePathsByType = mapOf("load" to "forge-conditions.json", "loot-table" to "forge-loot-conditions.json")),
}
