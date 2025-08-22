package juuxel.adorn.gradle.datagen;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DataOutputImpl;
import juuxel.adorn.datagen.tag.TagGenerator;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
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
    @Input
    public abstract SetProperty<DataConfig> getConfigs();

    @Input
    public abstract Property<Boolean> getGenerateTags();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    @TaskAction
    public void generate() {
        var workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(GenerateAction.class, parameters -> {
            parameters.getConfigs().set(getConfigs());
            parameters.getGenerateTags().set(getGenerateTags());
            parameters.getOutput().set(getOutput());
        });
    }

    public interface Parameters extends WorkParameters {
        @Input
        SetProperty<DataConfig> getConfigs();

        @Input
        Property<Boolean> getGenerateTags();

        @OutputDirectory
        DirectoryProperty getOutput();
    }

    public abstract static class GenerateAction implements WorkAction<Parameters> {
        @Inject
        public GenerateAction() {
        }

        @Override
        public void execute() {
            var params = getParameters();
            var output = DataOutputImpl.load(params.getOutput().get().getAsFile().toPath());
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

            output.finish();
        }
    }
}
