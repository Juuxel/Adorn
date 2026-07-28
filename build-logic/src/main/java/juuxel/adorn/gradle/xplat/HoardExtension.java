package juuxel.adorn.gradle.xplat;

import juuxel.adorn.gradle.action.ZipTransformerAction;
import juuxel.adorn.gradle.util.zip.InjectJijMetadata;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import javax.inject.Inject;
import java.util.Objects;

public abstract class HoardExtension {
    public abstract Property<String> getModId();
    public abstract Property<String> getGroup();
    public abstract Property<String> getVersion();

    @Inject
    protected abstract Project getProject();

    private final TaskProvider<? extends Jar> hoardJarTask;

    public HoardExtension(TaskProvider<? extends Jar> hoardJarTask) {
        this.hoardJarTask = hoardJarTask;

        getModId().convention("adorn_resources");
        getGroup().convention(getProject().provider(() -> Objects.toString(getProject().getGroup())));
        getVersion().convention(getProject().provider(() -> Objects.toString(getProject().getVersion())));
    }

    public TaskProvider<? extends Jar> getHoardJarTask() {
        return hoardJarTask;
    }

    /// Adds resources untracked by a `ProcessResources` task to a source set.
    /// Using this for large directories speeds up compiling in dev considerably.
    public void addResources(SourceSet sourceSet, Object resourceDir) {
        // Add to runtime classpath
        addResourcesToRuntimeClasspath(sourceSet, resourceDir);

        // Add to built jar
        getHoardJarTask().configure(task -> {
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

    public void injectToFabricMod() {
        injectToMod(InjectJijMetadata.FabricModJson.class);
    }

    public void injectToNeoForgeMod() {
        injectToMod(InjectJijMetadata.NeoForgeJarJar.class);
    }

    private void injectToMod(Class<? extends InjectJijMetadata> actionClass) {
        getProject().getTasks().named(JavaPlugin.JAR_TASK_NAME, Jar.class, task -> {
            task.from(getHoardJarTask().flatMap(Jar::getArchiveFile), spec -> {
                spec.into("META-INF/jars");
            });

            var step = getProject().getObjects().newInstance(actionClass);
            configureInjectJijStep(step);
            task.doLast(new ZipTransformerAction(transformer -> transformer.addStep(step)));
        });
    }

    private void configureInjectJijStep(InjectJijMetadata step) {
        step.getGroup().set(getGroup());
        step.getModId().set(getModId());
        step.getVersion().set(getVersion());
        step.getFilePath().set(getHoardJarTask().flatMap(Jar::getArchiveFileName).map(name -> "META-INF/jars/" + name));
    }
}
