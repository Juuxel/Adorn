package juuxel.adorn.datagen

enum class ConditionType(
    private val id: String,
    /** The template for the file path to an external condition file. */
    val separateFilePathTemplate: String? = null,
    /** The path to the template for the text of an external condition file. */
    val separateFileTemplatePath: String? = null,
    val conditionsInFileTemplatePathsByType: Map<String, String> = emptyMap()
) {
    NONE("none"),
    FABRIC("fabric", conditionsInFileTemplatePathsByType = mapOf("load" to "fabric-conditions.json")),
    FORGE("forge", conditionsInFileTemplatePathsByType = mapOf("load" to "forge-conditions.json", "loot-table" to "forge-loot-conditions.json")),
    ;

    companion object {
        private val BY_ID = values().associateBy { it.id }

        fun parse(id: String): ConditionType? =
            if (id.isEmpty()) NONE
            else BY_ID[id]
    }
}
