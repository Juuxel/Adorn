package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.ConditionType
import juuxel.adorn.datagen.Generator
import juuxel.adorn.datagen.Material
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.WoodMaterial
import juuxel.adorn.datagen.WoolMaterial
import juuxel.adorn.datagen.buildTemplateApplier
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
    abstract val woolMaterials: SetProperty<WoolMaterial>

    @get:Input
    abstract val conditionType: Property<ConditionType>

    @get:Internal
    abstract val exclusions: MapProperty<Material, Set<String>>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @TaskAction
    fun generate() {
        val outputPath = output.get().asFile.toPath()
        val exclusions = exclusions.get()

        if (Files.exists(outputPath)) {
            // See https://stackoverflow.com/a/35989142
            Files.walk(outputPath).use {
                it.sorted(Comparator.reverseOrder()).forEach { child -> Files.delete(child) }
            }
        }

        val cache = TemplateCache()
        generate(outputPath, Generator.STONE_GENERATORS, stoneMaterials.get(), cache, exclusions)
        generate(outputPath, Generator.WOOD_GENERATORS, woodMaterials.get(), cache, exclusions)
        generate(outputPath, Generator.WOOL_GENERATORS, woolMaterials.get(), cache, exclusions)
    }

    private fun <M : Material> generate(outputPath: Path, gens: List<Generator<M>>, mats: Set<M>, templateCache: TemplateCache, exclusions: Map<Material, Set<String>>) {
        for (gen in gens) {
            val templateText = templateCache.load(gen.templatePath)

            for (mat in mats) {
                if (exclusions[mat]?.contains(gen.id) == true) continue

                val applier = buildTemplateApplier {
                    init(mat)
                    gen.substitutionConfig(this, mat)
                }
                val output = applier.apply(templateText)
                val filePathStr = applier.apply(gen.outputPathTemplate)
                val filePath = outputPath.resolve(filePathStr)
                Files.createDirectories(filePath.parent)
                Files.writeString(filePath, output)

                if (gen.requiresCondition && mat.isModded()) {
                    val condType = conditionType.get()
                    if (condType.separateFilePathTemplate != null) {
                        val conditionTemplate = templateCache.load(condType.separateFileTemplatePath!!)
                        val conditionApplier = buildTemplateApplier {
                            "mod-id" with mat.prefix.namespace
                            "file-path" with filePathStr
                        }
                        val conditionText = conditionApplier.apply(conditionTemplate)
                        val conditionPathStr = conditionApplier.apply(condType.separateFilePathTemplate)
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
