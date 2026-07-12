package juuxel.adorn.gradle;

import juuxel.adorn.gradle.translation.TranslationExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;

public final class TranslationPlugin implements Plugin<Project> {
    @SuppressWarnings("unchecked")
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(CorePlugin.class);
        var extension = CorePlugin.registerExtension(project, "translations", TranslationExtension.class);

        // Set up dependencies
        var kelp = project.getConfigurations().dependencyScope("kelp");
        var kelpCp = project.getConfigurations().resolvable(
            "kelpClasspath",
            c -> c.extendsFrom(kelp)
        );
        project.getDependencies().addProvider(kelp.getName(), extension.getKelpVersion().map(v -> "io.github.juuxel:kelp:" + v));

        project.getTasks().register("reformatTranslations", JavaExec.class, task -> {
            task.setGroup(CorePlugin.TASK_GROUP);
            task.classpath(kelpCp);
            task.getMainModule().set("io.github.juuxel.translationtool");

            extension.getTranslationDir().finalizeValue();
            var dir = extension.getTranslationDir().get().getAsFile().getAbsolutePath();
            task.args("--reformat", dir);
        });

        project.getTasks().register("editTranslations", JavaExec.class, task -> {
            task.setGroup(CorePlugin.TASK_GROUP);
            task.classpath(kelpCp);
            task.getMainModule().set("io.github.juuxel.translationtool");

            extension.getTranslationDir().finalizeValue();
            var dir = extension.getTranslationDir().get().getAsFile().getAbsolutePath();
            task.args(dir);
        });
    }
}
