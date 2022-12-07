package juuxel.adorn.datagen.gradle

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
            TagGenerator.generate(
                configs = parameters.configs.map { it.toPath() },
                outputDirectory = parameters.output.get().asFile.toPath()
            )
        }
    }
}
