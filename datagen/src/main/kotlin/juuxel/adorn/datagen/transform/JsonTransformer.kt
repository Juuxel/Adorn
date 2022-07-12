package juuxel.adorn.datagen.transform

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Path

class JsonTransformer(private val transformations: List<TransformSet>) {
    fun apply(path: Path) {
        val rawJson = JsonSlurper().parse(path)
        if (rawJson !is MutableMap<*, *>) return
        val original = HashMap(rawJson)
        @Suppress("UNCHECKED_CAST")
        val json = rawJson as MutableMap<String, Any?>

        outer@ for (t in transformations) {
            for ((key, value) in t.requirements) {
                if (json[key] != value) continue@outer
            }

            for (key in t.remove) {
                json.remove(key)
            }

            for ((key, value) in t.add) {
                json[key] = value
            }
        }

        if (original != json) {
            Files.writeString(path, JsonOutput.toJson(json))
        }
    }
}
