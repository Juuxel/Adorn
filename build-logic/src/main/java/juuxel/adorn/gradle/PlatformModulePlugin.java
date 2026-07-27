package juuxel.adorn.gradle;

import juuxel.adorn.gradle.action.ZipTransformerAction;
import juuxel.adorn.gradle.util.zip.InlineServiceLoader;
import juuxel.adorn.gradle.util.zip.MinifyJson;
import juuxel.adorn.gradle.xplat.PlatformModuleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.api.ModSettings;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.bundling.Jar;

import java.util.Map;

public final class PlatformModulePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(MinecraftSetupPlugin.class);
        var extension = CorePlugin.registerExtension(project, "platformModule", PlatformModuleExtension.class);
        var loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        project.getTasks().named(JavaPlugin.JAR_TASK_NAME, Jar.class, task -> {
            // Include common files
            var commonSourceSets = getSourceSets(project.project(":common"));
            task.from(commonSourceSets.named("main").map(SourceSet::getOutput));

            task.filesMatching("**/package-info.class", details -> {
                details.setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE); // these are all the same anyway
            });

            // Add platform classifier
            task.getArchiveClassifier().set(extension.getPlatformName());

            task.doLast(new ZipTransformerAction(transformer -> {
                // Inline platform services
                transformer.addStep(new InlineServiceLoader());

                // Minify JSON files
                transformer.addStep(new MinifyJson());
            }));
        });

        // Generate IDE run configs for each run config.
        loom.getRuns().configureEach(run -> run.getGenerateRunConfig().set(true));

        // Set a different run directory for the server so the log and config files don't conflict.
        loom.getRuns().named("server", run -> run.getRunDirectory().set(project.file("run/server")));

        // Set up the access widener.
        loom.getAccessWidenerPath().set(extension.getAccessWidenerFile());

        // Set up mod entry. "main" matches the default NeoForge mod's name.
        ModSettings mod = loom.getMods().maybeCreate("main");
        mod.sourceSet(SourceSet.MAIN_SOURCE_SET_NAME);
        mod.sourceSet(SourceSet.MAIN_SOURCE_SET_NAME, ":common");

        // Depend on the common project.
        project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, createCommonDependency(project));
    }

    private static Dependency createCommonDependency(Project project) {
        var commonDependency = project.getDependencies().project(Map.of("path", ":common"));
        ((ModuleDependency) commonDependency).setTransitive(false);
        return commonDependency;
    }

    private static SourceSetContainer getSourceSets(Project project) {
        return project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
    }
}
