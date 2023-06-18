package juuxel.adorn.datagen

interface TemplateDsl {
    fun init(material: Material)
    infix fun String.with(other: String)
    infix fun String.with(id: Id) = withId(id.namespace, id.path)
    fun String.withId(namespace: String, path: String)
    infix fun String.withId(id: String) = with(Id.parse(id))
    fun putAll(properties: Map<String, String>)
}

fun buildSubstitutions(block: TemplateDsl.() -> Unit): Map<String, String> =
    TemplateBuilder().apply(block).build()

private class TemplateBuilder : TemplateDsl {
    private val substitutions = HashMap<String, String>()

    override fun init(material: Material) {
        substitutions["mod-id"] =
            if (material.isModded()) material.id.namespace
            else ""

        substitutions["mod-prefix"] =
            if (material.isModded()) "${material.id.namespace}/"
            else ""

        "stick" with material.stick
        with(material) { appendTemplates() }
    }

    override fun String.with(other: String) {
        substitutions[this] = other
    }

    override fun String.withId(namespace: String, path: String) {
        substitutions[this] = "$namespace:$path"
        substitutions["$this.namespace"] = namespace
        substitutions["$this.path"] = path
    }

    override fun putAll(properties: Map<String, String>) =
        substitutions.putAll(properties)

    private fun resolve(key: String): String {
        var current: String = substitutions[key]!!

        while ('<' in current) {
            current = TemplateApplier.apply(current, substitutions)
        }

        return current
    }

    fun build(): Map<String, String> {
        val result = HashMap<String, String>()

        for (key in substitutions.keys) {
            result[key] = resolve(key)
        }

        return result
    }
}
