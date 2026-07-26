package juuxel.adorn.datagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DataOutputImpl implements DataOutput {
    private final Path directory;
    private final List<Path> existing = new ArrayList<>();
    private final Map<String, String> originalHashes = new HashMap<>();
    private final Map<String, String> newHashes = new TreeMap<>();

    public DataOutputImpl(Path directory) {
        this.directory = directory;
    }

    public void loadHashes(Reader reader) throws IOException {
        var buffered = new BufferedReader(reader);
        String line;
        while ((line = buffered.readLine()) != null) {
            var split = line.split("\t", 2);
            var hash = split[0];
            var file = split[1];
            originalHashes.put(file, hash);

            var path = directory.resolve(file);
            if (Files.exists(path)) {
                existing.add(path);
            }
        }
    }

    public void saveHashes(Appendable app) throws IOException {
        for (var entry : newHashes.entrySet()) {
            app.append(entry.getValue()).append('\t').append(entry.getKey()).append('\n');
        }
    }

    @Override
    public void write(String path, String content) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        var hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        var humanReadableHash = HexFormat.of().withUpperCase().formatHex(hashBytes);
        var outputPath = directory.resolve(path);
        boolean hadExisting = existing.remove(outputPath.toAbsolutePath());
        newHashes.put(path, humanReadableHash);
        if (!hadExisting || !humanReadableHash.equals(originalHashes.get(path))) {
            try {
                Files.createDirectories(outputPath.getParent());
                Files.writeString(outputPath, content);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public void finish() {
        try {
            try (var writer = Files.newBufferedWriter(directory.resolve(".cache"))) {
                saveHashes(writer);
            }

            for (var path : existing) {
                Files.delete(path);

                // TODO: Replace with deleting visitor like in loom
                var parent = path.getParent();
                while (parent != null && !Files.isSameFile(parent, directory) && isEmpty(parent)) {
                    Files.delete(parent);
                    parent = parent.getParent();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static boolean isEmpty(Path directory) throws IOException {
        try (var children = Files.list(directory)) {
            return children.findAny().isEmpty();
        }
    }

    public static DataOutputImpl load(Path directory) {
        var output = new DataOutputImpl(directory);
        var cachePath = directory.resolve(".cache");
        if (Files.exists(cachePath)) {
            try (var reader = Files.newBufferedReader(cachePath)) {
                output.loadHashes(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return output;
    }
}
