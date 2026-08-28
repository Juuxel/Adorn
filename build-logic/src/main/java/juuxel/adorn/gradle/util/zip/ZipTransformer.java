package juuxel.adorn.gradle.util.zip;

import org.intellij.lang.annotations.MagicConstant;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipTransformer {
    private final List<Step> steps = new ArrayList<>();
    private @Nullable CompressionMethod compressionMethod;

    public ZipTransformer addStep(Step step) {
        steps.add(step);
        return this;
    }

    public ZipTransformer compressionMethod(CompressionMethod compressionMethod) {
        this.compressionMethod = compressionMethod;
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

                if (entry.isDirectory()) {
                    zipOut.putNextEntry(entry);
                    zipOut.closeEntry();
                    continue;
                }

                byte[] bytes = null;

                for (var step : steps) {
                    if (!step.shouldApply(entry.getName())) continue;

                    if (bytes == null) {
                        try (var in = zipIn.getInputStream(entry)) {
                            bytes = in.readAllBytes();
                        }
                    }

                    bytes = Objects.requireNonNull(step.transform(entry.getName(), bytes, filer));
                }

                zipOut.putNextEntry(getEntryForOutput(entry, bytes));

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

    private ZipEntry getEntryForOutput(ZipEntry source, byte @Nullable [] data) {
        if (compressionMethod != null) {
            var copy = new ZipEntry(source);
            copy.setMethod(compressionMethod.id);

            if (compressionMethod == CompressionMethod.STORED) {
                if (data != null) {
                    // We must update the data length and CRC to match the new data.
                    // The zip writing errors otherwise.
                    copy.setSize(data.length);

                    var crc = new CRC32();
                    crc.update(data);
                    copy.setCrc(crc.getValue());
                }

                // Set the "compressed" size to match the real file size.
                copy.setCompressedSize(copy.getSize());
            }

            return copy;
        }

        return source;
    }

    @FunctionalInterface
    public interface Filer {
        byte @Nullable [] readFile(String path) throws IOException;
    }

    public interface Step {
        boolean shouldApply(String name);
        byte[] transform(String name, byte[] input, Filer filer) throws IOException;
    }

    public enum CompressionMethod {
        DEFLATED(ZipEntry.DEFLATED),
        STORED(ZipEntry.STORED);

        @ZipMethodInt
        private final int id;

        CompressionMethod(@ZipMethodInt int id) {
            this.id = id;
        }
    }

    @MagicConstant(valuesFromClass = ZipEntry.class)
    private @interface ZipMethodInt {
    }
}
