package juuxel.adorn.datagen

internal class TemplateApplier(private val substitutions: Map<String, String>) {
    fun apply(text: String): String {
        var current = text

        for ((key, value) in substitutions) {
            current = current.replace("<$key>", value)
        }

        return current
    }
}
