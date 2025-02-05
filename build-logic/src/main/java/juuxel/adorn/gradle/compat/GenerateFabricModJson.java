package juuxel.adorn.gradle.compat;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public abstract class GenerateFabricModJson extends DefaultTask {
    @Input
    public abstract Property<String> getVersion();

    @Input
    public abstract Property<String> getModId();

    @Input
    public abstract Property<String> getTargetModName();

    @Input
    public abstract Property<String> getTargetModId();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @Inject
    public GenerateFabricModJson() {
        getVersion().convention(getProject().provider(() -> Objects.toString(getProject().getVersion())));
        getModId().convention(getTargetModId().map(modId -> "adorn_integrations_" + modId));
    }

    @TaskAction
    public void run() throws IOException {
        var fmj = """
            {
              "schemaVersion": 1,
              "id": "%s",
              "name": "Adorn Integrations: %s",
              "version": "%s",
              "description": "Integrates features from %s into Adorn.",
              "authors": ["Juuz"],
              "license": "MIT",
              "contact": {
                "homepage": "https://modrinth.com/mod/adorn",
                "sources": "https://github.com/Juuxel/Adorn",
                "issues": "https://github.com/Juuxel/Adorn/issues"
              },
              "icon": "assets/adorn/icon.png",
            
              "depends": {
                "adorn": "*",
                "%s": "*"
              }
            }
            """.formatted(
                getModId().get(),
                getTargetModName().get(),
                getVersion().get(),
                getTargetModName().get(),
                getTargetModId().get()
            );
        var output = getOutputFile().get().getAsFile().toPath();
        Files.createDirectories(output.getParent());
        Files.writeString(output, fmj);
    }
}
