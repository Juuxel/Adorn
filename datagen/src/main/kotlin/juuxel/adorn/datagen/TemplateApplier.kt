package juuxel.adorn.datagen

internal object TemplateApplier {
    fun apply(text: String, substitutions: Map<String, String>): String {
        var current = text

        for ((key, value) in substitutions) {
            current = current.replace("<$key>", value)
        }

        return current
    }
}
