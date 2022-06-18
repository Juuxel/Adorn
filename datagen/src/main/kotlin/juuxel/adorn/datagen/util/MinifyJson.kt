package juuxel.adorn.datagen.util

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.Action
import org.gradle.api.Task
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files

/**
 * A simple action that you can add to a `Jar` task
 * that minifies all JSON files using [JsonSlurper] and [JsonOutput].
 */
object MinifyJson : Action<Task> {
    override fun execute(t: Task) {
        val jar = t.outputs.files.singleFile.toPath()
        FileSystems.newFileSystem(URI.create("jar:${jar.toUri()}"), mapOf("create" to false)).use { fs ->
            for (root in fs.rootDirectories) {
                Files.walk(root).filter { Files.isRegularFile(it) && it.toString().endsWith(".json") }.use { jsons ->
                    for (json in jsons) {
                        try {
                            Files.writeString(json, JsonOutput.toJson(JsonSlurper().parse(json)))
                        } catch (e: IOException) {
                            throw UncheckedIOException("Could not minify $json in $jar", e)
                        }
                    }
                }
            }
        }
    }
}
