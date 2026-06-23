package juuxel.adorn.gradle.compatchecker;

import juuxel.adorn.datagen.DataGenerator;
import juuxel.adorn.datagen.DependencyOutput;
import juuxel.adorn.datagen.Id;
import juuxel.adorn.gradle.CorePlugin;
import juuxel.adorn.gradle.datagen.DataConfig;
import juuxel.adorn.gradle.xplat.MinecraftExtension;
import juuxel.adorn.gradle.xplat.PlatformModuleExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CheckModDataCompat extends DefaultTask {
    @Input
    public abstract SetProperty<DataConfig> getConfigs();

    @Input
    public abstract Property<String> getGameVersion();

    @Input
    public abstract Property<String> getLoader();

    @Input
    public abstract SetProperty<Mod> getMods();

    @Input
    public abstract Property<Boolean> getForceRedownload();

    @OutputDirectory
    public abstract DirectoryProperty getModJarCache();

    public CheckModDataCompat() {
        getGameVersion().convention(CorePlugin.getExtension(getProject(), MinecraftExtension.class).getMinecraftVersion());
        getLoader().convention(CorePlugin.getExtension(getProject(), PlatformModuleExtension.class).getPlatformName());
        getModJarCache().convention(getProject().getLayout().getBuildDirectory().dir(getName() + "Mods"));
        getForceRedownload().convention(getProject().getGradle().getStartParameter().isRefreshDependencies());
        setGroup("verification");
    }

    public void mod(String modId) {
        mod(modId, modId);
    }

    public void mod(String modId, String slug) {
        var mod = getProject().getObjects().newInstance(Mod.class);
        mod.getModId().set(modId);
        mod.getSlug().set(slug);
        getMods().add(mod);
    }

    @TaskAction
    protected void check() throws IOException {
        Path modJarCache = getModJarCache().get().getAsFile().toPath();
        Files.createDirectories(modJarCache);

        String mc = getGameVersion().get();
        String loader = getLoader().get();
        boolean forceRedownload = getForceRedownload().get();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor();
             var downloader = new ModrinthModDownloader(executor)) {
            record ModFile(String id, JarIndex index) {
            }

            // download and index all jars
            List<CompletableFuture<ModFile>> jarIndexFutures = new ArrayList<>();

            for (Mod mod : getMods().get()) {
                String id = mod.getModId().get();
                jarIndexFutures.add(
                    downloader.findLatestVersion(mod.getSlug().get(), loader, mc)
                        .thenCompose(metadata -> {
                            Path indexPath = modJarCache.resolve(metadata.fileName() + ".index.json.gz");

                            if (forceRedownload || !Files.exists(indexPath)) {
                                // If redownloading mods or the index is missing, generate a new one
                                return downloader.download(metadata, modJarCache, forceRedownload)
                                    .thenApply(path -> {
                                        try {
                                            // Extract nested mods
                                            Set<Path> paths = new HashSet<>();
                                            Path jijDir = modJarCache.resolve(id);
                                            Files.createDirectories(jijDir);
                                            JarInJarExtractor.extractJarInJar(jijDir, path, paths, forceRedownload);

                                            // Build index
                                            JarIndex index;
                                            try (var vfs = Vfs.ofZips(paths)) {
                                                index = JarIndex.ofVfs(vfs);
                                            }
                                            index.write(indexPath);

                                            // Delete mod files (they can be very big)
                                            for (Path downloadedJar : paths) {
                                                Files.delete(downloadedJar);
                                            }
                                            Files.delete(jijDir);

                                            return new ModFile(id, index);
                                        } catch (IOException e) {
                                            throw new UncheckedIOException(e);
                                        }
                                    });
                            } else {
                                try {
                                    var index = JarIndex.read(indexPath);
                                    return CompletableFuture.completedFuture(new ModFile(id, index));
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                            }
                        })
                );
            }

            // read all configs
            Set<Id> textures = new HashSet<>();
            Set<Id> items = new HashSet<>();
            Function<DataConfig, Stream<Path>> configFileGetter = config -> config.getFiles().getFiles().stream().map(File::toPath);
            var generatorBuilder = DataGenerator.builder(
                getConfigs().get().stream()
                    .filter(config -> !config.getTagsOnly().get())
                    .flatMap(configFileGetter)
                    .toList()
            );
            generatorBuilder.generateDependencies(new DependencyOutput() {
                @Override
                public void acceptTexture(Id texture) {
                    textures.add(texture);
                }

                @Override
                public void acceptItem(Id item) {
                    items.add(item);
                }
            });
            generatorBuilder.build().generate();

            // check
            List<String> missingFiles = new ArrayList<>();

            for (CompletableFuture<ModFile> future : jarIndexFutures) {
                future.thenAcceptAsync(modFile -> {
                    List<Id> texturesToCheck = textures.stream().filter(id -> id.namespace().equals(modFile.id)).toList();
                    List<Id> itemsToCheck = items.stream().filter(id -> id.namespace().equals(modFile.id)).toList();
                    var checker = new ModCompatChecker(texturesToCheck, itemsToCheck, modFile.index);
                    synchronized (missingFiles) {
                        missingFiles.addAll(checker.check());
                    }
                }, executor).join();
            }

            if (!missingFiles.isEmpty()) {
                Collections.sort(missingFiles);
                StringBuilder sb = new StringBuilder("Missing ").append(missingFiles.size()).append(" files:");

                for (String file : missingFiles) {
                    sb.append('\n').append(file);
                }

                throw new RuntimeException(sb.toString());
            }
        }
    }

    public interface Mod {
        Property<String> getModId();
        Property<String> getSlug();
    }
}
