package juuxel.adorn.gradle;

import juuxel.adorn.gradle.datagen.DataGeneratorExtension;
import juuxel.adorn.gradle.datagen.GenerateData;
import juuxel.adorn.gradle.datagen.GenerateDataCode;
import juuxel.adorn.gradle.datagen.GenerateEmi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;
import java.util.ArrayList;

public final class ModularDataGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(CorePlugin.class);
        var extension = CorePlugin.registerExtension(project, "dataGenerator", DataGeneratorExtension.class);

        project.getTasks().register("generateAllData");

        project.afterEvaluate(p -> {
            for (var config : extension.getSettings()) {
                setup(project, config);
            }
        });
    }

    private static void setup(Project project, DataGeneratorExtension.Settings settings) {
        var sourceSet = settings.getSourceSet().get();
        var generatedResources = settings.getGeneratedResources();
        var generateMainData = project.getTasks().register(sourceSet.getTaskName("generate", "MainData"), GenerateData.class, task -> {
            task.getConfigs().set(settings.getConfigs());
            task.getGenerateTags().set(settings.getGenerateTags());
            task.getOutput().convention(generatedResources);
        });
        var generateData = project.getTasks().register(sourceSet.getTaskName("generate", "Data"), task -> task.dependsOn(generateMainData));
        project.getTasks().named("generateAllData", task -> task.dependsOn(generateData));
        project.getTasks().named(sourceSet.getProcessResourcesTaskName(), task -> task.mustRunAfter(generateData));
        sourceSet.getResources().srcDir(generatedResources);
        sourceSet.getResources().exclude(".cache");

        if (settings.getGenerateEmiFiles().get()) {
            var generateEmi = project.getTasks().register(sourceSet.getTaskName("generate", "Emi"), GenerateEmi.class, task -> {
                task.mustRunAfter(generateMainData);
                task.getOutput().convention(generateMainData.flatMap(GenerateData::getOutput));
                task.getModId().set(settings.getModId());
                var resourceDirs = new ArrayList<>(sourceSet.getResources().getSrcDirs());

                if (settings.getIncludeCommonFilesInEmi().get()) {
                    resourceDirs.addAll(getSourceSets(project.project(":common")).getByName("main").getResources().getSrcDirs());
                }

                for (File dir : resourceDirs) {
                    task.getRecipes().from(project.fileTree(dir, tree -> {
                        tree.include("**/data/adorn/recipe/**");

                        // The unpacking recipes create "uncraftable" vanilla items like
                        // nether wart, so exclude them.
                        tree.exclude("**/data/adorn/recipe/crates/unpack/**");
                    }));
                }
            });
            generateData.configure(task -> task.dependsOn(generateEmi));
        }

        if (settings.getGenerateCode().get()) {
            var generatedSources = settings.getGeneratedSources();
            var generateDataCode = project.getTasks().register(sourceSet.getTaskName("generate", "DataCode"), GenerateDataCode.class, task -> {
                task.getConfigs().set(settings.getConfigs());
                task.getOutput().convention(generatedSources);
            });
            sourceSet.getJava().srcDir(generateDataCode.flatMap(GenerateDataCode::getOutput));
        }
    }

    private static SourceSetContainer getSourceSets(Project project) {
        return project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
    }
}
