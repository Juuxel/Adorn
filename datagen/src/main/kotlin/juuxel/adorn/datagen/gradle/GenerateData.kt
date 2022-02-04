package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Generator
import juuxel.adorn.datagen.GeneratorConfig
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.TemplateApplier
import juuxel.adorn.datagen.GeneratorConfigLoader
import juuxel.adorn.datagen.buildSubstitutions
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path

abstract class GenerateData : DefaultTask() {
    @get:InputFiles
    abstract val configs: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @TaskAction
    fun generate() {
        val outputPath = output.get().asFile.toPath()

        if (Files.exists(outputPath)) {
            // See https://stackoverflow.com/a/35989142
            Files.walk(outputPath).use {
                it.sorted(Comparator.reverseOrder()).forEach { child -> Files.delete(child) }
            }
        }

        val cache = TemplateCache()

        for (configFile in configs) {
            generate(outputPath, GeneratorConfigLoader.read(configFile.toPath()), cache)
        }
    }

    private fun generate(outputPath: Path, config: GeneratorConfig, cache: TemplateCache) {
        val stoneMaterials = config.stones
        generate(outputPath, Generator.STONE_GENERATORS, stoneMaterials, cache, config.conditionType)
        generate(outputPath, Generator.SIDED_STONE_GENERATORS, stoneMaterials.filter { it.material.hasSidedTexture }, cache, config.conditionType)
        generate(
            outputPath,
            Generator.UNSIDED_STONE_GENERATORS,
            stoneMaterials.filter { !it.material.hasSidedTexture },
            cache,
            config.conditionType
        )
        generate(outputPath, Generator.WOOD_GENERATORS, config.woods, cache, config.conditionType)
        generate(outputPath, Generator.WOOL_GENERATORS, config.wools, cache, config.conditionType)
    }

    private fun <M : Material> generate(
        outputPath: Path,
        gens: List<Generator<M>>,
        mats: Iterable<GeneratorConfig.MaterialEntry<M>>,
        templateCache: TemplateCache,
        conditionType: ConditionType
    ) {
        for (gen in gens) {
            val templateText = templateCache.load(gen.templatePath)

            for ((mat, exclude, replace) in mats) {
                if (gen.id in exclude) continue

                val mainSubstitutions = buildSubstitutions {
                    "advancement-condition" with "<load-condition>"
                    "loot-table-condition" with "<load-condition>"
                    "recipe-condition" with "<load-condition>"
                    "load-condition" with ""

                    for ((type, path) in conditionType.conditionsInFileTemplatePathsByType) {
                        "$type-condition" with templateCache.load(path)
                    }

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
                    if (conditionType.separateFilePathTemplate != null) {
                        val conditionTemplate = templateCache.load(conditionType.separateFileTemplatePath!!)
                        val conditionSubstitutions = buildSubstitutions {
                            "mod-id" with mat.id.namespace
                            "file-path" with filePathStr
                        }
                        val conditionText = TemplateApplier.apply(conditionTemplate, conditionSubstitutions)
                        val conditionPathStr = TemplateApplier.apply(conditionType.separateFilePathTemplate, conditionSubstitutions)
                        val conditionPath = outputPath.resolve(conditionPathStr)
                        Files.createDirectories(conditionPath.parent)
                        Files.writeString(conditionPath, conditionText)
                    }
                }
            }
        }
    }

    private class TemplateCache {
        private val cache = HashMap<String, String>()

        fun load(path: String): String =
            cache.getOrPut(path) {
                GenerateData::class.java.getResourceAsStream("/adorn/templates/$path").use {
                    it!!.bufferedReader().readText()
                }
            }
    }
}
