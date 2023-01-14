package juuxel.adorn.datagen

import java.nio.file.Files
import java.nio.file.Path

object DataGenerator {
    fun generate(configFiles: List<Path>, outputPath: Path) {
        if (Files.exists(outputPath)) {
            // See https://stackoverflow.com/a/35989142
            Files.walk(outputPath).use {
                it.sorted(Comparator.reverseOrder()).forEach { child -> Files.delete(child) }
            }
        }

        val cache = TemplateCache()

        for (configFile in configFiles) {
            val config = GeneratorConfigLoader.read(configFile)
            generate(outputPath, config, cache)

            val configName = configFile.fileName.toString().removeSuffix(".xml")
            generateBlockVariantIcons(config, configName, outputPath)
        }
    }

    private fun generate(outputPath: Path, config: GeneratorConfig, cache: TemplateCache) {
        val stoneMaterials = config.stones
        generate(outputPath, Generator.STONE_GENERATORS, stoneMaterials, cache, config)
        generate(outputPath, Generator.SIDED_STONE_GENERATORS, stoneMaterials.filter { it.material.hasSidedTexture }, cache, config)
        generate(
            outputPath,
            Generator.UNSIDED_STONE_GENERATORS,
            stoneMaterials.filter { !it.material.hasSidedTexture },
            cache,
            config
        )
        generate(outputPath, Generator.WOOD_GENERATORS, config.woods, cache, config)
        generate(outputPath, Generator.WOOL_GENERATORS, config.wools, cache, config)
    }

    private fun <M : Material> generate(
        outputPath: Path,
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
                val filePath = outputPath.resolve(filePathStr)
                Files.createDirectories(filePath.parent)
                Files.writeString(filePath, output)

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
                        val conditionPath = outputPath.resolve(conditionPathStr)
                        Files.createDirectories(conditionPath.parent)
                        Files.writeString(conditionPath, conditionText)
                    }
                }
            }
        }
    }

    private fun generateBlockVariantIcons(config: GeneratorConfig, name: String, outputPath: Path) {
        val content = BlockVariantGenerator.generateIconFile(config)

        if (content != null) {
            val path = outputPath.resolve("assets/adorn/adorn/block_variant_textures/$name.json")
            Files.createDirectories(path.parent)
            Files.writeString(path, content)
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
