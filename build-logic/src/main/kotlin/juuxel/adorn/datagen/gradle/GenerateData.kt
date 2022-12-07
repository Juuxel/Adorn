package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.DataGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor
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
            DataGenerator.generate(
                configFiles = parameters.configs.map { it.toPath() },
                outputPath = parameters.output.get().asFile.toPath()
            )
        }
    }
}
