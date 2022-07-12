package juuxel.adorn.datagen.gradle

import juuxel.adorn.datagen.transform.JsonTransformer
import juuxel.adorn.datagen.transform.TransformSet
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

abstract class TransformJson : DefaultTask() {
    @get:Input
    abstract val transformSets: MapProperty<String, List<TransformSet>>

    @get:OutputDirectory
    abstract val directory: DirectoryProperty

    @TaskAction
    fun execute() {
        val transformSetsByPath = transformSets.get()

        for ((path, transformSets) in transformSetsByPath) {
            val files = project.fileTree(directory) {
                include(path)
            }

            for (file in files.files) {
                JsonTransformer(transformSets).apply(file.toPath())
            }
        }
    }
}
