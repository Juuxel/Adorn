package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.Generator
import juuxel.adorn.datagen.GeneratorConfig
import juuxel.adorn.datagen.GeneratorConfigLoader
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.TemplateApplier
import juuxel.adorn.datagen.buildSubstitutions
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

abstract class GenerateData : DefaultTask() {
    @get:InputFiles
    abstract val configs: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @get:Inject
    protected abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun generate() {
        workerExecutor.noIsolation().submit(GenerationAction::class.java) {
            configs.from(this@GenerateData.configs)
            output.set(this@GenerateData.output)
        }
    }

    interface GenerationParameters : WorkParameters {
        val configs: ConfigurableFileCollection
        val output: DirectoryProperty
    }

    abstract class GenerationAction : WorkAction<GenerationParameters> {
        override fun execute() {
            val outputPath = parameters.output.get().asFile.toPath()

            if (Files.exists(outputPath)) {
                // See https://stackoverflow.com/a/35989142
                Files.walk(outputPath).use {
                    it.sorted(Comparator.reverseOrder()).forEach { child -> Files.delete(child) }
                }
            }

            val cache = TemplateCache()

            for (configFile in parameters.configs) {
                generate(outputPath, GeneratorConfigLoader.read(configFile.toPath()), cache)
            }
        }
    }

    companion object {
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
