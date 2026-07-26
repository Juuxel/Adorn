package juuxel.adorn.data;

import juuxel.adorn.datagen.DataOutput;
import juuxel.adorn.datagen.tag.TagGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class AdornTagGenerator extends AdornCustomDataGenerator {
    private static final String TAG_CONFIG_DIRS_PROPERTY = "adorn.data.tagConfigDirs";

    public AdornTagGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    protected void run(DataOutput output) {
        var configFiles = getDataConfigs(TAG_CONFIG_DIRS_PROPERTY);
        TagGenerator.generateFromConfigFiles(configFiles, output);
    }

    @Override
    public String getName() {
        return "Tags";
    }

    static List<Path> getDataConfigs(String systemProperty) {
        return Arrays.stream(System.getProperty(systemProperty).split(File.pathSeparator))
            .map(Path::of)
            .flatMap(path -> {
                // Support subprojects without src/data.
                if (Files.notExists(path)) return Stream.empty();

                if (Files.isDirectory(path)) {
                    try {
                        return Files.list(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                } else {
                    return Stream.of(path);
                }
            })
            .filter(path -> path.getFileName().toString().endsWith(".xml"))
            .toList();
    }
}
