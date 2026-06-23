package juuxel.adorn.gradle.compatchecker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

public final class JarInJarExtractor {
    private static final String NEOFORGE_JAR_JAR_METADATA_PATH = "META-INF/jarjar/metadata.json";

    public static void extractJarInJar(Path directory, Path modJar, Set<Path> jars, boolean overwriteExisting) throws IOException {
        jars.add(modJar);
        Gson gson = new Gson();

        try (var zip = new ZipFile(modJar.toFile())) {
            var neoForgeJarJarEntry = zip.getEntry(NEOFORGE_JAR_JAR_METADATA_PATH);
            if (neoForgeJarJarEntry != null) {
                try (var in = zip.getInputStream(neoForgeJarJarEntry);
                     var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    var metadata = gson.fromJson(reader, NeoForgeJarJarMetadata.class);
                    for (NeoForgeJarJarJar jar : metadata.jars) {
                        Path extracted = extractNested(directory, zip, jar.path, overwriteExisting);
                        extractJarInJar(directory, extracted, jars, overwriteExisting);
                    }
                }
            }

            var fmjEntry = zip.getEntry("fabric.mod.json");
            if (fmjEntry != null) {
                try (var in = zip.getInputStream(fmjEntry);
                     var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    var fmj = gson.fromJson(reader, JsonObject.class);
                    var jarsArray = fmj.getAsJsonArray("jars");
                    if (jarsArray != null) {
                        for (JsonElement jar : jarsArray) {
                            String path = jar.getAsJsonObject().getAsJsonPrimitive("file").getAsString();
                            Path extracted = extractNested(directory, zip, path, overwriteExisting);
                            extractJarInJar(directory, extracted, jars, overwriteExisting);
                        }
                    }
                }
            }
        }
    }

    private static Path extractNested(Path directory, ZipFile zip, String entry, boolean overwriteExisting) throws IOException {
        int slashIndex = entry.lastIndexOf('/');
        String fileName = slashIndex >= 0 ? entry.substring(slashIndex + 1) : entry;
        Path targetPath = directory.resolve(fileName);
        if (!overwriteExisting && Files.exists(targetPath)) return targetPath;
        try (var in = zip.getInputStream(zip.getEntry(entry))) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return targetPath;
    }

    public record NeoForgeJarJarMetadata(List<NeoForgeJarJarJar> jars) {
    }

    public record NeoForgeJarJarJar(String path) {
    }
}
