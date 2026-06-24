package juuxel.adorn.gradle.datagen;

import juuxel.adorn.datagen.GeneratorConfig;
import juuxel.adorn.datagen.GeneratorConfigLoader;
import juuxel.adorn.gradle.util.DeletingFileVisitor;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GenerateDataCode extends DefaultTask {
    @Nested
    public abstract SetProperty<DataConfig> getConfigs();

    @OutputDirectory
    public abstract DirectoryProperty getOutput();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    @TaskAction
    protected void generate() {
        var workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(GenerateAction.class, parameters -> {
            parameters.getConfigs().set(getConfigs());
            parameters.getOutput().set(getOutput());
        });
    }

    public interface Parameters extends WorkParameters {
        @Nested
        SetProperty<DataConfig> getConfigs();

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
            Function<DataConfig, Stream<Path>> configFileGetter = config -> config.getFiles().getFiles().stream().map(File::toPath);
            var outputDir = params.getOutput().get().getAsFile().toPath();

            try {
                Files.walkFileTree(outputDir, new DeletingFileVisitor());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            var configFilesIter = params.getConfigs().get().stream()
                .filter(config -> !config.getTagsOnly().get())
                .flatMap(configFileGetter)
                .iterator();

            while (configFilesIter.hasNext()) {
                var configFile = configFilesIter.next();

                try {
                    var config = GeneratorConfigLoader.read(configFile);
                    var className = config.className();
                    if (className.isEmpty()) continue;

                    if (!config.colors().isEmpty()) {
                        throw new IllegalArgumentException("Cannot generate class file " + className + " for data file " + configFile + " with colour materials");
                    }

                    var pkg = className.substring(0, className.lastIndexOf('.'));
                    var simpleName = className.substring(pkg.length() + 1);
                    var targetPath = outputDir.resolve(className.replace('.', '/') + ".java");
                    Files.createDirectories(targetPath.getParent());
                    var srcJoiner = new StringJoiner(
                        "\n",
                        """
                        package %s;

                        import juuxel.adorn.block.variant.BlockVariant;
                        import juuxel.adorn.block.variant.BlockVariantSet;

                        import java.util.List;

                        public final class %s implements BlockVariantSet {
                        """.formatted(
                            pkg,
                            simpleName
                        ),
                        "}"
                    );
                    formatEntries(srcJoiner, config.woods(), "Wood");
                    formatEntries(srcJoiner, config.stones(), "Stone");
                    Files.writeString(targetPath, srcJoiner.toString(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }

        private static void formatEntries(StringJoiner target, Collection<? extends GeneratorConfig.MaterialEntry<?>> entries, String type) {
            if (entries.isEmpty()) return;

            var methodJoiner = new StringJoiner(
                ",\n            ",
                """
                    @Override
                    public List<BlockVariant> get%sVariants() {
                        return List.of(
                            \
                """.formatted(type),
                """

                        );
                    }
                """
            );

            for (GeneratorConfig.MaterialEntry<?> entry : entries) {
                methodJoiner.add(formatEntry(entry, type));
            }

            target.add(methodJoiner.toString());
        }

        private static String formatEntry(GeneratorConfig.MaterialEntry<?> entry, String type) {
            var id = entry.material().getId();
            return "new BlockVariant." + type + "(\"" + id.namespace() + '/' + id.path() + "\")";
        }
    }
}
