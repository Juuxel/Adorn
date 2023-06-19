package juuxel.adorn.datagen

import java.nio.file.Path

object DataGenerator {
    fun generate(configFiles: List<Path>, output: DataOutput) {
        val cache = TemplateCache()

        for (configFile in configFiles) {
            generate(output, GeneratorConfigLoader.read(configFile), cache)
        }
    }

    private fun generate(output: DataOutput, config: GeneratorConfig, cache: TemplateCache) {
        val stoneMaterials = config.stones
        generate(output, Generator.STONE_GENERATORS, stoneMaterials, cache, config)
        generate(output, Generator.SIDED_STONE_GENERATORS, stoneMaterials.filter { it.material.hasSidedTexture }, cache, config)
        generate(
            output,
            Generator.UNSIDED_STONE_GENERATORS,
            stoneMaterials.filter { !it.material.hasSidedTexture },
            cache,
            config
        )
        generate(output, Generator.WOOD_GENERATORS, config.woods, cache, config)
        generate(output, Generator.WOOL_GENERATORS, config.wools, cache, config)
    }

    private fun <M : Material> generate(
        dataOutput: DataOutput,
        gens: List<Generator<M>>,
        mats: Iterable<GeneratorConfig.MaterialEntry<M>>,
        templateCache: TemplateCache,
        config: GeneratorConfig,
    ) {
        val conditionType = config.conditionType
        for (gen in gens) {
            val templateText = templateCache.load(gen.templatePath)

            for ((mat, exclude, replace) in mats) {
                if (gen.id in exclude) continue

                val mainSubstitutions = buildSubstitutions {
                    "wood_texture_separator" with "_"
                    "advancement-condition" with "<load-condition>"
                    "loot-table-condition" with "<load-condition>"
                    "recipe-condition" with "<load-condition>"
                    "load-condition" with ""

                    for ((type, path) in conditionType.conditionsInFileTemplatePathsByType) {
                        "$type-condition" with templateCache.load(path)
                    }

                    putAll(config.rootReplacements)
                    init(mat)
                    gen.substitutionConfig(this, mat)
                    putAll(replace)
                }
                val output = TemplateApplier.apply(templateText, mainSubstitutions)
                val filePathStr = TemplateApplier.apply(gen.outputPathTemplate, mainSubstitutions)
                dataOutput.write(filePathStr, output)

                if (gen.requiresCondition && mat.isModded()) {
                    val externalConditionPathTemplate = conditionType.separateFilePathTemplate
                    if (externalConditionPathTemplate != null) {
                        val conditionTemplate = templateCache.load(conditionType.separateFileTemplatePath!!)
                        val conditionSubstitutions = buildSubstitutions {
                            "mod-id" with mat.id.namespace
                            "file-path" with filePathStr
                        }
                        val conditionText = TemplateApplier.apply(conditionTemplate, conditionSubstitutions)
                        val conditionPathStr = TemplateApplier.apply(externalConditionPathTemplate, conditionSubstitutions)
                        dataOutput.write(conditionPathStr, conditionText)
                    }
                }
            }
        }
    }

    private class TemplateCache {
        private val cache = HashMap<String, String>()

        fun load(path: String): String =
            cache.getOrPut(path) {
                DataGenerator::class.java.getResourceAsStream("/adorn/templates/$path").use {
                    it!!.bufferedReader().readText()
                }
            }
    }
}
