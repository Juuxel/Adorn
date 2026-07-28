package juuxel.adorn.gradle.xplat;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

import javax.inject.Inject;

public abstract class AdornExtension implements ExtensionAware {
    @Inject
    protected abstract Project getProject();

    /// Adds resources untracked by a `ProcessResources` task to a source set.
    /// Using this for large directories speeds up compiling in dev considerably.
    public void addResources(SourceSet sourceSet, Object resourceDir) {
        getProject().getLogger().lifecycle(":adding {} for {} in {}", resourceDir, sourceSet.getName(), getProject().getPath());

        // Add to runtime classpath
        addResourcesToRuntimeClasspath(sourceSet, resourceDir);

        // Add to built jar
        getProject().getTasks().named(JavaPlugin.JAR_TASK_NAME, Jar.class, task -> {
            task.from(getProject().fileTree(resourceDir), spec -> spec.exclude(".cache"));
        });

        var loom = getProject().getExtensions().findByType(LoomGradleExtensionAPI.class);
        if (loom == null) return;

        // Add to Loom mod entry
        loom.getMods().named("main", mod -> mod.getModFiles().from(resourceDir));

        // Loom uses the runtimeClasspath configuration for adding the main runtime classpath to the client one,
        // but we're adding directly to the file collection. Hence, we have to add it separately to the client one as well.
        if (sourceSet.getName().equals(SourceSet.MAIN_SOURCE_SET_NAME) && loom.areEnvironmentSourceSetsSplit()) {
            var sourceSets = getProject().getExtensions().getByType(JavaPluginExtension.class).getSourceSets();
            addResourcesToRuntimeClasspath(sourceSets.getByName("client"), resourceDir);
        }
    }

    private void addResourcesToRuntimeClasspath(SourceSet sourceSet, Object resourceDir) {
        sourceSet.setRuntimeClasspath(sourceSet.getRuntimeClasspath().plus(getProject().files(resourceDir)));
    }
}
