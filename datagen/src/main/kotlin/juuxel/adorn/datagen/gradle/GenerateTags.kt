package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.GeneratorConfigLoader
import juuxel.adorn.datagen.tag.TagGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkerExecutor
import java.nio.file.Files
import javax.inject.Inject

abstract class GenerateTags : DefaultTask() {
    @get:InputFiles
    @get:SkipWhenEmpty
    abstract val configs: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @get:Inject
    protected abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun generate() {
        workerExecutor.noIsolation().submit(GenerationAction::class.java) {
            configs.from(this@GenerateTags.configs)
            output.set(this@GenerateTags.output)
        }
    }

    abstract class GenerationAction : WorkAction<GenerateData.GenerationParameters> {
        override fun execute() {
            val materials = parameters.configs.asSequence()
                .sortedBy { it.absolutePath }
                .map {
                    GeneratorConfigLoader.read(it.toPath())
                }
                .flatMap {
                    sequence {
                        yieldAll(it.woods)
                        yieldAll(it.stones)
                        yieldAll(it.wools)
                    }
                }
                .map { it.material }
                .toList()
                .distinctBy { it.id }
            val outputDir = parameters.output.get().asFile.toPath()

            for ((id, generator) in TagGenerator.GENERATORS_BY_ID) {
                for (tagType in arrayOf("blocks", "items")) {
                    val path = outputDir.resolve("data/${id.namespace}/tags/$tagType/${id.path}.json")
                    Files.createDirectories(path.parent)
                    Files.writeString(path, generator.generate(materials))
                }
            }
        }
    }
}
