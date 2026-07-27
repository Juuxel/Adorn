package juuxel.adorn.gradle;

import juuxel.adorn.gradle.xplat.MinecraftExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePluginExtension;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.compile.JavaCompile;

public final class MinecraftSetupPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply("dev.architectury.loom-no-remap");
        project.getPlugins().apply(CorePlugin.class);
        var extension = CorePlugin.registerExtension(project, "minecraft", MinecraftExtension.class);

        // Copy the artifact metadata from the root project.
        Project rootProject = project.getRootProject();
        project.setGroup(rootProject.getGroup());
        project.setVersion(rootProject.getVersion());
        getBase(project).getArchivesName().set(getBase(rootProject).getArchivesName());

        extension.getMinecraftVersion().convention(project.getProviders().gradleProperty("minecraft-version"));

        // Set up Java version
        var java = project.getExtensions().getByType(JavaPluginExtension.class);
        java.setSourceCompatibility(JavaVersion.VERSION_25);
        java.setTargetCompatibility(JavaVersion.VERSION_25);
        project.getTasks().withType(JavaCompile.class).configureEach(task -> {
            task.getOptions().getRelease().set(25);
        });

        // Make all archives reproducible.
        project.getTasks().withType(AbstractArchiveTask.class).configureEach(task -> {
            task.setReproducibleFileOrder(true);
            task.setPreserveFileTimestamps(false);
        });

        // Include the license in the jar files.
        project.getTasks().named("jar", Jar.class, task -> {
            task.from(rootProject.file("LICENSE"));
        });

        var loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        // Disable the mixin annotation processor on all platforms
        loom.getMixin().getUseLegacyMixinAp().set(false);

        // Set the Minecraft dependency.
        project.getDependencies().add("minecraft", extension.getMinecraftVersion().map(version -> "net.minecraft:minecraft:" + version));

        // Generate package-info files with the @NullMarked annotation for the main source set.
        extension.generatePackageInfos(java.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME));
    }

    private static BasePluginExtension getBase(Project project) {
        return project.getExtensions().getByType(BasePluginExtension.class);
    }
}
