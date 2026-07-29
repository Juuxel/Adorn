package juuxel.adorn.gradle.translation;

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

public abstract class TranslationExtension {
    public abstract Property<String> getKelpVersion();
    public abstract DirectoryProperty getTranslationDir();

    @Inject
    protected abstract Project getProject();

    public TranslationExtension() {
        getKelpVersion().convention("1.1.1");
        getTranslationDir().convention(
            getProject().getLayout().dir(
                getProject().provider(() -> getProject().file("src/main/resources/assets/adorn/lang"))
            )
        );
    }
}
