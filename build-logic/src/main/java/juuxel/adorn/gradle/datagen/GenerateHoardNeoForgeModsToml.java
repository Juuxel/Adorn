package juuxel.adorn.gradle.datagen;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public abstract class GenerateHoardNeoForgeModsToml extends DefaultTask {
    private static final String MODS_TOML =
        """
        license = "%s"

        [[mods]]
        modId = "%s"
        version = "%s"
        displayName = "Adorn Resources"
        description = "Adorn resources."
        """;

    @Input
    public abstract Property<String> getModId();

    @Input
    public abstract Property<String> getVersion();

    @Input
    public abstract Property<String> getLicense();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    public GenerateHoardNeoForgeModsToml() {
        getLicense().convention("MIT");
    }

    @TaskAction
    protected void generate() {
        var workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(GenerateAction.class, parameters -> {
            parameters.getModId().set(getModId());
            parameters.getVersion().set(getVersion());
            parameters.getLicense().set(getLicense());
            parameters.getOutputFile().set(getOutputFile());
        });
    }

    public interface Parameters extends WorkParameters {
        @Input
        Property<String> getModId();

        @Input
        Property<String> getVersion();

        @Input
        Property<String> getLicense();

        @OutputFile
        RegularFileProperty getOutputFile();
    }

    public abstract static class GenerateAction implements WorkAction<Parameters> {
        @Override
        public void execute() {
            try {
                run();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void run() throws IOException {
            var params = getParameters();
            var toml = MODS_TOML.formatted(params.getLicense().get(), params.getModId().get(), params.getVersion().get());
            Files.writeString(params.getOutputFile().get().getAsFile().toPath(), toml, StandardCharsets.UTF_8);
        }
    }
}
