package juuxel.adorn.gradle;

import juuxel.adorn.gradle.xplat.AdornExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePluginExtension;

public final class CorePlugin implements Plugin<Project> {
    public static final String TASK_GROUP = "adorn";

    @Override
    public void apply(Project project) {
        project.getPlugins().apply("base");

        var providers = project.getProviders();
        var base = project.getExtensions().getByType(BasePluginExtension.class);
        base.getArchivesName().set(providers.gradleProperty("archives-name"));
        project.setGroup(providers.gradleProperty("group").get());
        project.setVersion(providers.gradleProperty("mod-version").get() + "+" + providers.gradleProperty("minecraft-version").get());

        project.getExtensions().create("adorn", AdornExtension.class);
    }

    public static AdornExtension getExtension(Project project) {
        return project.getExtensions().getByType(AdornExtension.class);
    }

    public static <T> T getExtension(Project project, Class<T> extensionClass) {
        return getExtension(project).getExtensions().getByType(extensionClass);
    }

    public static <T> T registerExtension(Project project, String name, Class<T> extensionClass, Object... args) {
        return getExtension(project).getExtensions().create(name, extensionClass, args);
    }
}
