package juuxel.adorn.gradle;

import juuxel.adorn.gradle.xplat.AdornExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public final class CorePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("adorn", AdornExtension.class);
    }

    public static <T> T getExtension(Project project, Class<T> extensionClass) {
        return project.getExtensions().getByType(AdornExtension.class).getExtensions().getByType(extensionClass);
    }

    public static <T> T registerExtension(Project project, String name, Class<T> extensionClass) {
        return project.getExtensions().getByType(AdornExtension.class).getExtensions().create(name, extensionClass);
    }
}
