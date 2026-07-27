package juuxel.adorn.gradle.packageinfo;

import juuxel.adorn.gradle.util.DeletingFileVisitor;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import org.gradle.workers.WorkerExecutor;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public abstract class GeneratePackageInfos extends DefaultTask {
    private static final String GENERATED_PACKAGE_INFO =
        """
        @NullMarked
        package %s;

        import org.jspecify.annotations.NullMarked;
        """;

    @InputDirectory
    public abstract DirectoryProperty getSourceRoot();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();
    
    @TaskAction
    protected void run() {
        var workQueue = getWorkerExecutor().noIsolation();
        workQueue.submit(GenerateAction.class, params -> {
            params.getSourceRoot().set(getSourceRoot());
            params.getOutputDirectory().set(getOutputDirectory());
        });
    }

    public interface Parameters extends WorkParameters {
        @InputDirectory
        DirectoryProperty getSourceRoot();

        @OutputDirectory
        DirectoryProperty getOutputDirectory();
    }

    public static abstract class GenerateAction implements WorkAction<Parameters> {
        @Override
        public void execute() {
            try {
                run();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void run() throws IOException {
            Path sourceRoot = getParameters().getSourceRoot().get().getAsFile().toPath().toAbsolutePath();
            Path outputDir = getParameters().getOutputDirectory().get().getAsFile().toPath();
            Files.walkFileTree(outputDir, new DeletingFileVisitor());
            Files.walkFileTree(sourceRoot, new SimpleFileVisitor<>() {
                private final Set<Path> dirsWithJava = new HashSet<>();

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().endsWith(".java")) {
                        dirsWithJava.add(file.getParent());
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                    if (dirsWithJava.contains(dir)) {
                        Path relativeDir = sourceRoot.relativize(dir.toAbsolutePath());
                        String packageName = relativeDir.toString().replace(File.separator, ".");
                        Path packageInfoPath = outputDir.resolve(relativeDir).resolve("package-info.java");
                        Files.createDirectories(packageInfoPath.getParent());
                        Files.writeString(packageInfoPath, GENERATED_PACKAGE_INFO.formatted(packageName), StandardCharsets.UTF_8);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
