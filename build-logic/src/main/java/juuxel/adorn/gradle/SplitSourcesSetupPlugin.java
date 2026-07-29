package juuxel.adorn.gradle;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.type.ArtifactTypeDefinition;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.bundling.Jar;

import java.io.File;

public final class SplitSourcesSetupPlugin implements Plugin<Project> {
    public static final String CLIENT_OUTPUTS_CONFIGURATION_NAME = "clientOutputs";

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
        project.getConfigurations().consumable(CLIENT_OUTPUTS_CONFIGURATION_NAME, config -> {
            config.attributes(attrs -> {
                attrs.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY));
                attrs.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL));
                attrs.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.JAR));
            });

            config.getOutgoing().artifact(clientJar, artifact -> artifact.setType(ArtifactTypeDefinition.JAR_TYPE));

            var variants = config.getOutgoing().getVariants();

            variants.create("classes", variant -> {
                variant.getAttributes().attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.CLASSES));

                for (File dir : client.getOutput().getClassesDirs()) {
                    variant.artifact(dir, artifact -> {
                        artifact.builtBy(client.getClassesTaskName());
                        artifact.setType(ArtifactTypeDefinition.JVM_CLASS_DIRECTORY);
                    });
                }
            });

            variants.create("resources", variant -> {
                variant.getAttributes().attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.class, LibraryElements.RESOURCES));
                variant.artifact(client.getOutput().getResourcesDir(), artifact -> {
                    artifact.builtBy(client.getProcessResourcesTaskName());
                    artifact.setType(ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY);
                });
            });
        });
    }
}
