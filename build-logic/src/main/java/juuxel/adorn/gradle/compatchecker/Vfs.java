package juuxel.adorn.gradle.compatchecker;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public interface Vfs extends Closeable {
    boolean exists(String filePath);

    static Vfs ofZips(List<Path> zips) throws IOException {
        if (zips.size() == 1) return new Zip(zips.getFirst());

        List<Zip> zipVfs = new ArrayList<>(zips.size());

        for (Path zip : zips) {
            zipVfs.add(new Zip(zip));
        }

        return new Union(zipVfs);
    }

    final class Zip implements Vfs {
        private final ZipFile zip;

        public Zip(Path path) throws IOException {
            this.zip = new ZipFile(path.toFile());
        }

        @Override
        public boolean exists(String filePath) {
            return zip.getEntry(filePath) != null;
        }

        @Override
        public void close() throws IOException {
            zip.close();
        }
    }

    record Union(List<? extends Vfs> children) implements Vfs {
        @Override
        public boolean exists(String filePath) {
            for (Vfs child : children) {
                if (child.exists(filePath)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void close() throws IOException {
            for (Vfs child : children) {
                child.close();
            }
        }
    }
}
