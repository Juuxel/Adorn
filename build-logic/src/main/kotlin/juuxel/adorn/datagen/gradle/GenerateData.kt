package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.DataGenerator
import juuxel.adorn.datagen.tag.TagGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GenerateData : DefaultTask() {
    @get:Internal
    abstract val configs: SetProperty<DataConfig>

    @get:Input
    abstract val generateTags: Property<Boolean>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    init {
        // Not exactly complete (ignores moving files between configs, so their tagsOnly changes)
        // but close enough for my uses. :^)
        inputs.files(configs.map { it.map { config -> config.files } }).skipWhenEmpty()
        inputs.property("configs.tagsOnly", configs.map { it.associate { config -> config.name to config.tagsOnly } })
    }

    @TaskAction
    fun generate() {
        DataGenerator.generate(
            configFiles = configs.get()
                .filter { !it.tagsOnly.get() }
                .flatMap { config -> config.files.map { it.toPath() } },
            outputPath = output.get().asFile.toPath()
        )

        if (generateTags.get()) {
            TagGenerator.generate(
                configs = configs.get().flatMap { config -> config.files.map { it.toPath() } },
                outputDirectory = output.get().asFile.toPath()
            )
        }
    }
}
