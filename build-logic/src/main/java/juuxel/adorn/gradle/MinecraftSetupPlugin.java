package juuxel.adorn.gradle;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePluginExtension;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.compile.JavaCompile;

public final class MinecraftSetupPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply("dev.architectury.loom");
        project.getPlugins().apply(CorePlugin.class);

        // Copy the artifact metadata from the root project.
        Project rootProject = project.getRootProject();
        project.setGroup(rootProject.getGroup());
        project.setVersion(rootProject.getVersion());
        getBase(project).getArchivesName().set(getBase(rootProject).getArchivesName());

        // Set up Java version
        var java = project.getExtensions().getByType(JavaPluginExtension.class);
        java.setSourceCompatibility(JavaVersion.VERSION_21);
        java.setTargetCompatibility(JavaVersion.VERSION_21);
        project.getTasks().withType(JavaCompile.class).configureEach(task -> {
            task.getOptions().getRelease().set(21);
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

        // Set the Minecraft dependency. The rootProject.property calls read from gradle.properties (and a variety of other sources).
        project.getDependencies().add("minecraft", "net.minecraft:minecraft:" + rootProject.property("minecraft-version"));

        // Set up the layered mappings with Yarn, a NeoForge compatibility patch and my Menu mappings.
        project.getDependencies().add("mappings", loom.layered(spec -> {
            spec.mappings("net.fabricmc:yarn:%s+%s:v2".formatted(rootProject.property("minecraft-version"), rootProject.property("yarn-mappings")));
            spec.mappings("dev.architectury:yarn-mappings-patch-neoforge:" + rootProject.property("neoforge-mappings-patch-version"));
            var menuVersion = rootProject.property("menu-mappings").toString();
            spec.mappings("io.github.juuxel:menu:" + menuVersion, m -> {
                m.enigmaMappings();
                m.mappingPath("Menu-%s/mappings".formatted(menuVersion.replace('+', '-')));
            });
        }));
    }

    private static BasePluginExtension getBase(Project project) {
        return project.getExtensions().getByType(BasePluginExtension.class);
    }
}
