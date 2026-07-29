package juuxel.adorn.gradle;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.bundling.Jar;

import java.io.File;

public final class SplitSourcesSetupPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(MinecraftSetupPlugin.class);

        var loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
        loom.splitEnvironmentSourceSets();

        var sourceSets = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
        var client = sourceSets.getByName("client");

        var clientJar = project.getTasks().register("clientJar", Jar.class, task -> {
            task.getDestinationDirectory().set(project.getLayout().getBuildDirectory().dir("devlibs"));
            task.getArchiveClassifier().set("client");
            task.from(client.getOutput());
        });

        var objects = project.getObjects();
        project.getConfigurations().consumable("clientOutputs", config -> {
            config.getOutgoing().artifact(clientJar);

            config.getOutgoing().artifact(client.getOutput().getResourcesDir(), artifact -> {
                artifact.builtBy(client.getProcessResourcesTaskName());
            });

            for (File dir : client.getOutput().getClassesDirs()) {
                config.getOutgoing().artifact(dir, artifact -> {
                    artifact.builtBy(client.getClassesTaskName());
                });
            }
        });
    }
}
