package juuxel.adorn.gradle.datagen;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class GenerateEmi extends DefaultTask {
    private static final String RECIPE_DIR = "/data/adorn/recipe/";

    @InputFiles
    @PathSensitive(PathSensitivity.ABSOLUTE)
    public abstract ConfigurableFileCollection getRecipes();

    @Input
    public abstract SetProperty<String> getPreferredRecipes();

    @Input
    public abstract Property<String> getModId();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();

    @SuppressWarnings("unchecked")
    @TaskAction
    public void generateRecipeDefaults() throws IOException {
        var recipesByResult = new HashMap<RecipeResult, RecipeData>();
        var preferredRecipes = getPreferredRecipes().get();
        for (var recipeFile : getRecipes()) {
            var json = (Map<String, ?>) new JsonSlurper().parse(recipeFile);
            var result = getResult(json);
            if (result == null) continue;
            var path = recipeFile.getAbsolutePath().replace(File.separator, "/");
            var recipeId = "adorn:" + path.substring(path.lastIndexOf(RECIPE_DIR) + RECIPE_DIR.length(), path.length() - ".json".length());
            var recipeData = new RecipeData(recipeId, fixType(json.get("type").toString()));
            var old = recipesByResult.putIfAbsent(result, recipeData);

            if (old != null && !old.id.equals(recipeId)) {
                RecipeData preferred;
                if (preferredRecipes.contains(old.id)) {
                    preferred = old;
                } else if (preferredRecipes.contains(recipeId)) {
                    preferred = recipeData;
                } else {
                    // Try a heuristic: if one recipe starts with another + "_from_",
                    // that should not be preferred
                    if (old.id.startsWith(recipeId + "_from_")) {
                        preferred = recipeData;
                    } else if (recipeId.startsWith(old.id + "_from_")) {
                        preferred = old;
                    } else {
                        // Still not? Alright, if one is a stonecutting recipe
                        // and the other one isn't, skip the stonecutting recipe.
                        if (old.type.equals("minecraft:stonecutting") && !recipeData.type.equals(old.type)) {
                            preferred = recipeData;
                        } else if (recipeData.type.equals("minecraft:stonecutting") && !recipeData.type.equals(old.type)) {
                            preferred = old;
                        } else {
                            throw new IllegalArgumentException("Duplicate recipes for %s: %s, %s".formatted(result, old, recipeId));
                        }
                    }
                }

                recipesByResult.put(result, preferred);
            }
        }

        var fileName = "assets/emi/recipe/defaults/%s.json".formatted(getModId().get());
        var outputPath = getOutput().get().getAsFile().toPath().resolve(fileName);
        var outputJson = Map.of("recipes", recipesByResult.values().stream().map(RecipeData::id).collect(Collectors.toCollection(TreeSet::new)));
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, JsonOutput.prettyPrint(JsonOutput.toJson(outputJson)));
    }

    @SuppressWarnings("unchecked")
    private @Nullable RecipeResult getResult(Map<String, ?> recipeJson) {
        var type = fixType(recipeJson.get("type").toString());
        return switch (type) {
            case "minecraft:crafting_shaped", "minecraft:crafting_shapeless", "adorn:brewing", "adorn:brewing_from_fluid" -> {
                var resultJson = (Map<String, ?>) recipeJson.get("result");
                yield new RecipeResult(resultJson.get("id").toString(), (Map<String, ?>) resultJson.get("components"));
            }

            case "minecraft:stonecutting" -> new RecipeResult(recipeJson.get("result").toString(), Map.of());
            case "adorn:fertilizer_refilling" -> null;

            default -> throw new IllegalArgumentException("Unknown recipe type: " + type);
        };
    }

    private String fixType(String type) {
        return type.indexOf(':') < 0 ? "minecraft:" + type : type;
    }

    private record RecipeData(String id, String type) {
    }

    private record RecipeResult(String id, Map<String, ?> components) {
        @Override
        public String toString() {
            if (components.isEmpty()) {
                return id;
            } else {
                return id + components;
            }
        }
    }
}
