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
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.bundling.Jar;

import java.util.Map;

public final class PlatformModulePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(MinecraftSetupPlugin.class);
        var extension = CorePlugin.registerExtension(project, "platformModule", PlatformModuleExtension.class);
        var loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        // Set up configurations to depend on common
        var objects = project.getObjects();
        var common = project.getConfigurations().dependencyScope("common");
        var commonClasses = project.getConfigurations().resolvable("commonClasses", config -> {
            config.getAttributes().attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.CLASSES));
            config.extendsFrom(common);
        });
        var commonResources = project.getConfigurations().resolvable("commonResources", config -> {
            config.getAttributes().attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.RESOURCES));
            config.extendsFrom(common);
        });

        project.getTasks().named(JavaPlugin.JAR_TASK_NAME, Jar.class, task -> {
            // Include common files
            task.from(commonClasses);
            task.from(commonResources);

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
        mod.sourceSet("client", ":common");

        // Depend on the common project.
        project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, createCommonDependency(project));

        if (project.getPlugins().hasPlugin(SplitSourcesSetupPlugin.class)) {
            project.getDependencies().add("clientImplementation", createCommonClientDependency(project));
        } else {
            project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, createCommonClientDependency(project));
        }

        project.getDependencies().add(common.getName(), createCommonDependency(project));
        project.getDependencies().add(common.getName(), createCommonClientDependency(project));
    }

    private static Dependency createCommonDependency(Project project) {
        var commonDependency = project.getDependencies().project(Map.of("path", ":common"));
        ((ModuleDependency) commonDependency).setTransitive(false);
        return commonDependency;
    }

    private static Dependency createCommonClientDependency(Project project) {
        var commonDependency = project.getDependencies().project(Map.of("path", ":common", "configuration", SplitSourcesSetupPlugin.CLIENT_OUTPUTS_CONFIGURATION_NAME));
        ((ModuleDependency) commonDependency).setTransitive(false);
        return commonDependency;
    }
}
