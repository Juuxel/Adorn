package juuxel.adorn.gradle.compatchecker;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface Vfs extends Closeable {
    Stream<String> files();

    static Vfs ofZips(Collection<Path> zips) throws IOException {
        if (zips.size() == 1) return new Zip(zips.iterator().next());

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
        public Stream<String> files() {
            return zip.stream()
                .filter(entry -> !entry.isDirectory())
                .map(ZipEntry::getName);
        }

        @Override
        public void close() throws IOException {
            zip.close();
        }
    }

    record Union(List<? extends Vfs> children) implements Vfs {
        @Override
        public Stream<String> files() {
            return children.stream().flatMap(Vfs::files);
        }

        @Override
        public void close() throws IOException {
            for (Vfs child : children) {
                child.close();
            }
        }
    }
}
