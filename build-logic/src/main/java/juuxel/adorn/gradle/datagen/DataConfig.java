package juuxel.adorn.gradle.datagen;

import org.gradle.api.Named;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;

import javax.inject.Inject;

public abstract class DataConfig implements Named {
    @InputFiles
    @PathSensitive(PathSensitivity.NONE)
    public abstract ConfigurableFileCollection getFiles();

    @Input
    public abstract Property<Boolean> getTagsOnly();

    // Override to add @Internal task input annotation
    @Internal
    @Override
    public abstract String getName();

    @Inject
    public DataConfig() {
        getTagsOnly().convention(false);
    }
}
