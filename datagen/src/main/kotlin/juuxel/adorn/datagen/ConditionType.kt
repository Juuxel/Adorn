package juuxel.adorn.datagen

enum class ConditionType(
    val separateFilePathTemplate: String? = null,
    val separateFileTemplatePath: String? = null,
    val conditionsInFileTemplatePathsByType: Map<String, String> = emptyMap()
) {
    NONE,
    LIBCD(separateFilePathTemplate = "<file-path>.mcmeta", separateFileTemplatePath = "libcd-conditions.mcmeta"),
    FABRIC(conditionsInFileTemplatePathsByType = mapOf("load" to "fabric-conditions.json")),
}
