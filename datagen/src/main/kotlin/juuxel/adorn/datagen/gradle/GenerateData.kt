package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Generator
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.WoodMaterial
import juuxel.adorn.datagen.ColorMaterial
import juuxel.adorn.datagen.Id
import juuxel.adorn.datagen.TemplateApplier
import juuxel.adorn.datagen.buildSubstitutions
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path

abstract class GenerateData : DefaultTask() {
    @get:Internal
    abstract val woodMaterials: SetProperty<WoodMaterial>

    @get:Internal
    abstract val stoneMaterials: SetProperty<StoneMaterial>

    @get:Internal
    abstract val colorMaterials: SetProperty<ColorMaterial>

    @get:Input
    abstract val allMaterials: SetProperty<Id>

    @get:Input
    abstract val conditionType: Property<ConditionType>

    @get:Input
    abstract val exclusions: MapProperty<Id, Set<String>>

    @get:Input
    abstract val extraProperties: MapProperty<Id, Map<String, String>>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    init {
        allMaterials.convention(
            woodMaterials.flatMap { woods ->
                stoneMaterials.flatMap { stones ->
                    colorMaterials.map { colors ->
                        HashSet<Id>().apply {
                            for (wood in woods) {
                                add(wood.id)
                            }

                            for (stone in stones) {
                                add(stone.id)
                            }

                            for (color in colors) {
                                add(color.id)
                            }
                        }
                    }
                }
            }
        )
    }

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
        val stoneMaterials = stoneMaterials.get()
        generate(outputPath, Generator.STONE_GENERATORS, stoneMaterials, cache)
        generate(outputPath, Generator.SIDED_STONE_GENERATORS, stoneMaterials.filter { it.hasSidedTexture }, cache)
        generate(outputPath, Generator.UNSIDED_STONE_GENERATORS, stoneMaterials.filter { !it.hasSidedTexture }, cache)
        generate(outputPath, Generator.WOOD_GENERATORS, woodMaterials.get(), cache)
        generate(outputPath, Generator.WOOL_GENERATORS, colorMaterials.get(), cache)
    }

    private fun <M : Material> generate(outputPath: Path, gens: List<Generator<M>>, mats: Iterable<M>, templateCache: TemplateCache) {
        for (gen in gens) {
            val templateText = templateCache.load(gen.templatePath)

            for (mat in mats) {
                if (exclusions.get()[mat.id]?.contains(gen.id) == true) continue

                val condType = conditionType.get()
                val mainSubstitutions = buildSubstitutions {
                    "advancement-condition" with "<load-condition>"
                    "loot-table-condition" with "<load-condition>"
                    "recipe-condition" with "<load-condition>"
                    "load-condition" with ""

                    for ((type, path) in condType.conditionsInFileTemplatePathsByType) {
                        "$type-condition" with templateCache.load(path)
                    }

                    init(mat)
                    gen.substitutionConfig(this, mat)
                    extraProperties.get()[mat.id]?.let { putAll(it) }
                }
                val output = TemplateApplier.apply(templateText, mainSubstitutions)
                val filePathStr = TemplateApplier.apply(gen.outputPathTemplate, mainSubstitutions)
                val filePath = outputPath.resolve(filePathStr)
                Files.createDirectories(filePath.parent)
                Files.writeString(filePath, output)

                if (gen.requiresCondition && mat.isModded()) {
                    if (condType.separateFilePathTemplate != null) {
                        val conditionTemplate = templateCache.load(condType.separateFileTemplatePath!!)
                        val conditionSubstitutions = buildSubstitutions {
                            "mod-id" with mat.id.namespace
                            "file-path" with filePathStr
                        }
                        val conditionText = TemplateApplier.apply(conditionTemplate, conditionSubstitutions)
                        val conditionPathStr = TemplateApplier.apply(condType.separateFilePathTemplate, conditionSubstitutions)
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
