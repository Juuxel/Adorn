package juuxel.adorn.datagen.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files

abstract class DeleteDuplicates : DefaultTask() {
    @get:InputDirectory
    abstract val generated: DirectoryProperty

    @get:OutputDirectory
    abstract val main: DirectoryProperty

    @TaskAction
    fun delete() {
        val generatedRoot = generated.get().asFile.toPath()

        Files.walk(generatedRoot).use { stream ->
            stream.filter(Files::isRegularFile)
                .forEach { Files.deleteIfExists(main.get().asFile.toPath().resolve(generatedRoot.relativize(it))) }
        }
    }
}
