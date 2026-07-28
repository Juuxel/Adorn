package juuxel.adorn.gradle.util.zip;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipTransformer {
    private final List<Step> steps = new ArrayList<>();

    public ZipTransformer addStep(Step step) {
        steps.add(step);
        return this;
    }

    public void transform(Path zip) throws IOException {
        var temp = zip.resolveSibling(zip.getFileName() + ".transformed");

        try (var zipIn = new ZipFile(zip.toFile());
             var out = Files.newOutputStream(temp);
             var zipOut = new ZipOutputStream(out)) {
            var entries = zipIn.entries();
            Filer filer = path -> {
                var entry = zipIn.getEntry(path);
                if (entry == null) return null;
                try (var in = zipIn.getInputStream(entry)) {
                    return in.readAllBytes();
                }
            };

            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();

                zipOut.putNextEntry(entry);
                byte[] bytes = null;

                for (var step : steps) {
                    if (entry.isDirectory() || !step.shouldApply(entry.getName())) continue;

                    if (bytes == null) {
                        try (var in = zipIn.getInputStream(entry)) {
                            bytes = in.readAllBytes();
                        }
                    }

                    bytes = Objects.requireNonNull(step.transform(entry.getName(), bytes, filer));
                }

                if (bytes != null) {
                    zipOut.write(bytes);
                } else {
                    try (var in = zipIn.getInputStream(entry)) {
                        in.transferTo(zipOut);
                    }
                }

                zipOut.closeEntry();
            }
        }

        Files.move(temp, zip, StandardCopyOption.REPLACE_EXISTING);
    }

    @FunctionalInterface
    public interface Filer {
        byte @Nullable [] readFile(String path) throws IOException;
    }

    public interface Step {
        boolean shouldApply(String name);
        byte[] transform(String name, byte[] input, Filer filer) throws IOException;
    }
}
