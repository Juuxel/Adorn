package juuxel.adorn.datagen

interface TemplateDsl {
    fun init(material: Material)
    infix fun String.with(other: String)
    infix fun String.with(id: Id)
}

internal fun buildTemplateApplier(block: TemplateDsl.() -> Unit): TemplateApplier =
    TemplateBuilder().apply(block).build()

private class TemplateBuilder : TemplateDsl {
    private val substitutions = HashMap<String, String>()

    override fun init(material: Material) {
        substitutions["mod-id"] =
            if (material.isModded()) "${material.prefix.namespace}"
            else ""

        substitutions["mod-prefix"] =
            if (material.isModded()) "${material.prefix.namespace}/"
            else ""

        "stick" with material.stick
        with(material) { appendTemplates() }
    }

    override fun String.with(other: String) {
        substitutions[this] = other
    }

    override fun String.with(id: Id) {
        substitutions[this] = id.toString()
        substitutions["$this.namespace"] = id.namespace
        substitutions["$this.path"] = id.path
    }

    fun build(): TemplateApplier =
        TemplateApplier(substitutions)
}
