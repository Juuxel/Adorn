package juuxel.adorn.gradle.util;

import net.fabricmc.loom.util.FileSystemUtil;
import org.gradle.api.file.FileCollection;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface JarView extends Closeable {
    Path getPath(String path);
    Stream<Path> walkFiles();

    static JarView open(FileCollection jars) throws IOException {
        List<JarView> views = new ArrayList<>();

        for (File file : jars) {
            var fs = FileSystemUtil.getJarFileSystem(file, false);
            views.add(new OfFileSystem(fs));
        }

        return views.size() > 1 ? new Union(views) : views.getFirst();
    }

    record OfFileSystem(FileSystemUtil.Delegate fs) implements JarView {
        @Override
        public Path getPath(String path) {
            return fs.getPath(path);
        }

        @Override
        public Stream<Path> walkFiles() {
            try {
                return Files.walk(fs.getPath("/"));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void close() throws IOException {
            fs.close();
        }
    }

    record Union(List<JarView> views) implements JarView {
        @Override
        public Path getPath(String path) {
            Path first = views.getFirst().getPath(path);
            if (Files.exists(first)) return first;

            for (int i = 1; i < views.size(); i++) {
                Path current = views.get(i).getPath(path);
                if (Files.exists(current)) return current;
            }

            return first;
        }

        @Override
        public Stream<Path> walkFiles() {
            return views.stream().flatMap(JarView::walkFiles);
        }

        @Override
        public void close() throws IOException {
            for (JarView view : views) {
                view.close();
            }
        }
    }
}
