package juuxel.adorn.gradle.compatchecker;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface ModDownloader extends AutoCloseable {
    CompletableFuture<Path> download(String id, String loader, String gameVersion, Path outputDirectory, boolean forceRedownload);

    @Override
    void close();
}
