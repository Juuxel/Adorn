package juuxel.adorn.datagen.gradle

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.util.TreeSet

abstract class GenerateEmi : DefaultTask() {
    @get:InputFiles
    abstract val recipes: ConfigurableFileCollection

    @get:Input
    abstract val preferredRecipes: SetProperty<String>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @Suppress("UNCHECKED_CAST")
    @TaskAction
    fun generateRecipeDefaults() {
        val recipesByResult = HashMap<String, RecipeData>()
        val preferredRecipes = preferredRecipes.get()
        for (recipeFile in recipes) {
            val json = JsonSlurper().parse(recipeFile) as Map<String, *>
            val result = getResult(json)
            val path = recipeFile.absolutePath.replace(File.separator, "/")
            val recipeId = "adorn:" + path.substringAfterLast("/data/adorn/recipes/").removeSuffix(".json")
            val recipeData = RecipeData(id = recipeId, type = fixType(json["type"].toString()))
            val old = recipesByResult.putIfAbsent(result, recipeData)

            if (old != null && old.id != recipeId) {
                val preferred = if (old.id in preferredRecipes) {
                    old
                } else if (recipeId in preferredRecipes) {
                    recipeData
                } else {
                    // Try a heuristic: if one recipe starts with another + "_from_",
                    // that should not be preferred
                    if (old.id.startsWith(recipeId + "_from_")) {
                        recipeData
                    } else if (recipeId.startsWith(old.id + "_from_")) {
                        old
                    } else {
                        // Still not? Alright, if one is a stonecutting recipe
                        // and the other one isn't, skip the stonecutting recipe.
                        if (old.type == "minecraft:stonecutting" && recipeData.type != old.type) {
                            recipeData
                        } else if (recipeData.type == "minecraft:stonecutting" && recipeData.type != old.type) {
                            old
                        } else {
                            throw IllegalArgumentException("Duplicate recipes for $result: $old, $recipeId")
                        }
                    }
                }

                recipesByResult[result] = preferred
            }
        }
        val outputPath = output.get().asFile.toPath()
            .resolve("assets/emi/recipe/defaults/adorn.json")
        val outputJson = mapOf("recipes" to recipesByResult.values.mapTo(TreeSet()) { it.id })
        Files.createDirectories(outputPath.parent)
        Files.writeString(outputPath, JsonOutput.prettyPrint(JsonOutput.toJson(outputJson)))
    }

    @Suppress("UNCHECKED_CAST")
    private fun getResult(recipeJson: Map<String, *>): String =
        when (val type = fixType(recipeJson["type"].toString())) {
            "minecraft:crafting_shaped", "minecraft:crafting_shapeless",
            "adorn:brewing", "adorn:brewing_from_fluid" ->
                (recipeJson["result"] as Map<String, *>)["item"].toString()

            "minecraft:stonecutting" ->
                recipeJson["result"].toString()

            else -> throw IllegalArgumentException("Unknown recipe type: $type")
        }

    private fun fixType(type: String): String =
        if (':' !in type) "minecraft:$type"
        else type

    private data class RecipeData(val id: String, val type: String)
}
