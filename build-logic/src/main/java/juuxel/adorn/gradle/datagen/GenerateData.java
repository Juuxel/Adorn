package juuxel.adorn.gradle.datagen;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DataOutputImpl;
import juuxel.adorn.datagen.tag.TagGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GenerateData extends DefaultTask {
    @Nested
    public abstract SetProperty<DataConfig> getConfigs();

    @Input
    public abstract Property<Boolean> getGenerateTags();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();

    /// The files that aren't deleted by the cache validator.
    /// These files are produced by another task in the output directory.
    @Input
    public abstract SetProperty<String> getPreservedFilePaths();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    @TaskAction
    public void generate() {
        var workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(GenerateAction.class, parameters -> {
            parameters.getConfigs().set(getConfigs());
            parameters.getGenerateTags().set(getGenerateTags());
            parameters.getOutput().set(getOutput());
            parameters.getPreservedFilePaths().set(getPreservedFilePaths());
        });
    }

    public interface Parameters extends WorkParameters {
        @Nested
        SetProperty<DataConfig> getConfigs();

        @Input
        Property<Boolean> getGenerateTags();

        @OutputDirectory
        DirectoryProperty getOutput();

        @Input
        SetProperty<String> getPreservedFilePaths();
    }

    public abstract static class GenerateAction implements WorkAction<Parameters> {
        @Inject
        public GenerateAction() {
        }

        @Override
        public void execute() {
            var params = getParameters();
            var directory = params.getOutput().get().getAsFile().toPath();
            var output = DataOutputImpl.load(directory);
            Function<DataConfig, Stream<Path>> configFileGetter = config -> config.getFiles().getFiles().stream().map(File::toPath);

            DataGenerator.generate(
                params.getConfigs().get().stream()
                    .filter(config -> !config.getTagsOnly().get())
                    .flatMap(configFileGetter)
                    .toList(),
                output
            );

            if (params.getGenerateTags().get()) {
                TagGenerator.generateFromConfigFiles(params.getConfigs().get().stream().flatMap(configFileGetter).toList(), output);
            }

            for (String path : params.getPreservedFilePaths().get()) {
                output.preserveFile(directory.resolve(path));
            }

            output.finish();
        }
    }
}
