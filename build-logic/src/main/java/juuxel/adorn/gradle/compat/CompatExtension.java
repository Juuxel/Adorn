package juuxel.adorn.gradle.compat;

import juuxel.adorn.gradle.CompatPlugin;
import juuxel.adorn.gradle.action.MinifyJson;
import juuxel.adorn.gradle.datagen.DataGeneratorExtension;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.bundling.Jar;

import javax.inject.Inject;

public abstract class CompatExtension {
    private final Project project;
    private final JavaPluginExtension java;
    private final DataGeneratorExtension dataGenerator;

    @Inject
    public CompatExtension(Project project) {
        this.project = project;
        this.java = project.getExtensions().getByType(JavaPluginExtension.class);
        this.dataGenerator = project.getExtensions().getByType(DataGeneratorExtension.class);
    }

    public void registerTargetMod(String modId, String modName, boolean fabric, boolean neo) {
        var compatSourceSet = java.getSourceSets().create(modId, sourceSet -> {
            var main = java.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            sourceSet.setCompileClasspath(sourceSet.getCompileClasspath().plus(main.getCompileClasspath()));
            sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath().plus(main.getRuntimeClasspath()));
            sourceSet.setAnnotationProcessorPath(sourceSet.getAnnotationProcessorPath().plus(main.getAnnotationProcessorPath()));
        });

        var jar = project.getTasks().register(modId + "Jar", Jar.class, task -> {
            task.from(compatSourceSet.getOutput());
            task.getArchiveClassifier().set(modId.replace('_', '-'));
            task.doLast(new MinifyJson());
        });
        project.getTasks().named("assemble", task -> task.dependsOn(jar));

        var dataConfig = project.file("src/data/" + modId + ".xml");
        boolean setupData = dataConfig.exists();

        if (setupData) {
            dataGenerator.getSettings().register(modId, settings -> {
                settings.getSourceSet().set(compatSourceSet);
                settings.getModId().set("adorn_integrations_" + modId);
                settings.getConfigs().register("main", config -> {
                    config.getFiles().from("src/data/" + modId + ".xml");
                });
            });
        }

        if (fabric) {
            project.getArtifacts().add(CompatPlugin.FABRIC_COMPAT_ARTIFACTS_CONFIGURATION, jar);

            var generateFmj = project.getTasks().register(compatSourceSet.getTaskName("generate", "FabricModJson"), GenerateFabricModJson.class, task -> {
                task.getTargetModId().set(modId);
                task.getTargetModName().set(modName);
                task.getOutputFile().set(project.getLayout().getBuildDirectory().file("mod-metadata/" + modId + "/fabric.mod.json"));
            });
            jar.configure(task -> task.from(generateFmj.flatMap(GenerateFabricModJson::getOutputFile)));
        }

        if (neo) {
            project.getArtifacts().add(CompatPlugin.NEOFORGE_COMPAT_ARTIFACTS_CONFIGURATION, jar);
        }
    }
}
