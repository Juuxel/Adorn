package juuxel.adorn.datagen

enum class ConditionType(
    val separateFilePathTemplate: String? = null,
    val separateFileTemplatePath: String? = null
) {
    NONE,
    LIBCD(separateFilePathTemplate = "<file-path>.mcmeta", separateFileTemplatePath = "libcd-conditions.mcmeta"),
}
