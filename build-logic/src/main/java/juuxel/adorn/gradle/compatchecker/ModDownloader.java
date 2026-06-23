package juuxel.adorn.gradle.compatchecker;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface ModDownloader<T> extends AutoCloseable {
    CompletableFuture<Path> download(ModFileMetadataProvider.ModFileMetadata<T> metadata, Path outputDirectory, boolean forceRedownload);

    @Override
    void close();
}
