package juuxel.adorn.gradle.compatchecker;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface ModDownloader<T> extends AutoCloseable {
    CompletableFuture<ModFileMetadata<T>> findLatestVersion(String id, String loader, String gameVersion);
    CompletableFuture<Path> download(ModFileMetadata<T> metadata, Path outputDirectory, boolean forceRedownload);

    @Override
    void close();

    record ModFileMetadata<T>(String fileName, T value) {
    }
}
