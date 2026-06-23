package juuxel.adorn.gradle.compatchecker;

import java.util.concurrent.CompletableFuture;

public interface ModFileMetadataProvider<T> extends AutoCloseable {
    CompletableFuture<ModFileMetadata<T>> findLatestVersion(String id, String loader, String gameVersion);

    @Override
    void close();

    record ModFileMetadata<T>(String fileName, T value) {
    }
}
