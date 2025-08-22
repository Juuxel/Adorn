package juuxel.adorn.gradle;

import juuxel.adorn.gradle.datagen.DataGeneratorExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;

public final class DataGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(ModularDataGeneratorPlugin.class);
        var java = project.getExtensions().getByType(JavaPluginExtension.class);
        var extension = CorePlugin.getExtension(project, DataGeneratorExtension.class);
        extension.getSettings().register("adorn", settings -> {
            settings.getSourceSet().set(java.getSourceSets().named(SourceSet.MAIN_SOURCE_SET_NAME));
            settings.getIncludeCommonFilesInEmi().set(true);
            settings.getModId().set("adorn");
            settings.getConfigs().register("main", config -> {
                config.getFiles().from(project.fileTree("src/data").filter(file -> file.getName().endsWith(".xml")));
            });
        });
    }
}
