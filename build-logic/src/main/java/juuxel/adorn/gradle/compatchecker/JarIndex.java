package juuxel.adorn.gradle.compatchecker;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public record JarIndex(List<String> files) {
    public static JarIndex ofVfs(Vfs vfs) {
        return new JarIndex(vfs.files().distinct().toList());
    }

    public static JarIndex read(Path path) throws IOException {
        try (var in = Files.newInputStream(path);
             var gz = new GZIPInputStream(in);
             var reader = new InputStreamReader(gz, StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, JarIndex.class);
        }
    }

    public void write(Path path) throws IOException {
        try (var out = Files.newOutputStream(path);
             var gz = new GZIPOutputStream(out);
             var writer = new OutputStreamWriter(gz, StandardCharsets.UTF_8)) {
            new Gson().toJson(this, writer);
        }
    }
}
