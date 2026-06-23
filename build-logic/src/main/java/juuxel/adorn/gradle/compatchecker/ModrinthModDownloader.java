package juuxel.adorn.gradle.compatchecker;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class ModrinthModDownloader implements ModDownloader<String> {
    private static final String API_URL = "https://api.modrinth.com/v2";
    private final HttpClient client;
    private final Gson gson = new Gson();

    public ModrinthModDownloader(Executor executor) {
        client = HttpClient.newBuilder().executor(executor).build();
    }

    @Override
    public CompletableFuture<ModFileMetadata<String>> findLatestVersion(String id, String loader, String gameVersion) {
        var loaders = URLEncoder.encode("[\"" + loader + "\"]", StandardCharsets.UTF_8);
        var gameVersions = URLEncoder.encode("[\"" + gameVersion + "\"]", StandardCharsets.UTF_8);
        var url = "%s/project/%s/version?loaders=%s&game_versions=%s".formatted(API_URL, id, loaders, gameVersions);
        var request = HttpRequest.newBuilder(URI.create(url)).build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(versionListResponse -> {
                if (versionListResponse.statusCode() != 200) {
                    throw new RuntimeException("Got code " + versionListResponse.statusCode() + " with body " + versionListResponse.body());
                }

                var versions = gson.fromJson(versionListResponse.body(), ModrinthVersionInfo[].class);
                Arrays.sort(versions, Comparator.comparing(ModrinthVersionInfo::datePublished).reversed());
                var latestFile = versions[0].findPrimaryFile();
                return new ModFileMetadata<>(latestFile.filename, latestFile.url);
            });
    }

    @Override
    public CompletableFuture<Path> download(ModFileMetadata<String> metadata, Path outputDirectory, boolean forceRedownload) {
        var targetPath = outputDirectory.resolve(metadata.fileName());

        if (forceRedownload || !Files.exists(targetPath)) {
            var fileRequest = HttpRequest.newBuilder(URI.create(metadata.value())).build();
            var bodyHandler = HttpResponse.BodyHandlers.ofFile(
                targetPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            return client.sendAsync(fileRequest, bodyHandler).thenApply(HttpResponse::body);
        } else {
            return CompletableFuture.completedFuture(targetPath);
        }
    }

    @Override
    public void close() {
        client.close();
    }

    public record ModrinthFileInfo(String filename, String url, boolean primary) {
    }

    public record ModrinthVersionInfo(@SerializedName("date_published") String datePublished, List<ModrinthFileInfo> files) {
        public ModrinthFileInfo findPrimaryFile() {
            for (ModrinthFileInfo file : files) {
                if (file.primary) {
                    return file;
                }
            }

            return files.getFirst();
        }
    }
}
