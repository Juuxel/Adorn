package juuxel.adorn.gradle;

import juuxel.adorn.gradle.action.InlineServiceLoader;
import juuxel.adorn.gradle.action.MinifyJson;
import juuxel.adorn.gradle.xplat.PlatformModuleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.api.ModSettings;
import net.fabricmc.loom.task.RemapJarTask;
import net.fabricmc.loom.util.Constants;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ModuleDependency;
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

        // Include common files into platform jars
        project.getTasks().named("jar", Jar.class, task -> {
            var commonSourceSets = getSourceSets(project.project(":common"));
            task.from(commonSourceSets.named("main").map(SourceSet::getOutput));
        });

        project.getTasks().named("remapJar", RemapJarTask.class, task -> {
            // Add platform classifier to final jar
            task.getArchiveClassifier().set(extension.getPlatformName());

            // Inline platform services
            task.doLast(new InlineServiceLoader());

            // Minify JSON files
            task.doLast(new MinifyJson());
        });

        var loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
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

        // Depend on the common project. The "namedElements" configuration contains the non-remapped
        // classes and resources of the project.
        // It follows Gradle's own convention of xyzElements for "outgoing" configurations like apiElements.
        var commonDependency = project.getDependencies().project(Map.of("path", ":common", "configuration", Constants.Configurations.NAMED_ELEMENTS));
        ((ModuleDependency) commonDependency).setTransitive(false);
        project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, commonDependency);
    }

    private static SourceSetContainer getSourceSets(Project project) {
        return project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
    }
}
