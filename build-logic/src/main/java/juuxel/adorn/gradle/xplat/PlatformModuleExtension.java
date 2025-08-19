package juuxel.adorn.gradle.xplat;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.File;
import java.util.Map;

public abstract class PlatformModuleExtension {
    private final Project project;

    public PlatformModuleExtension(Project project) {
        this.project = project;
        getPlatformName().convention(project.getName());

        Project common = project.project(":common");
        Provider<File> defaultAwFile = common.provider(() -> common.file("src/main/resources/adorn.accesswidener"));
        getAccessWidenerFile().convention(common.getLayout().file(defaultAwFile));
    }

    public abstract Property<String> getPlatformName();

    public abstract RegularFileProperty getAccessWidenerFile();

    public void setupVersionTemplating(String fileName) {
        project.getTasks().named(JavaPlugin.PROCESS_RESOURCES_TASK_NAME, ProcessResources.class).configure(task -> {
            task.getInputs().property("version", project.getVersion());
            task.filesMatching(fileName, details -> {
                details.expand(Map.of("version", task.getInputs().getProperties().get("version")));
            });
        });
    }
}
