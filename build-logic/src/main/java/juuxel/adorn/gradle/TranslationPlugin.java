package juuxel.adorn.gradle;

import juuxel.adorn.gradle.translation.TranslationExtension;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskProvider;

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

        registerKelpTask(project, "reformatTranslations", kelpCp, extension, task -> task.args("--reformat"));
        registerKelpTask(project, "editTranslations", kelpCp, extension, task -> {});
        var lintTask = registerKelpTask(project, "lintTranslations", kelpCp, extension, task -> task.args("--lint"));
        project.getTasks().named("check", task -> task.dependsOn(lintTask));
    }

    private static TaskProvider<JavaExec> registerKelpTask(Project project, String name, Object classpath, TranslationExtension extension, Action<? super JavaExec> config) {
        return project.getTasks().register(name, JavaExec.class, task -> {
            task.setGroup(CorePlugin.TASK_GROUP);
            task.classpath(classpath);
            task.getMainModule().set("io.github.juuxel.translationtool");

            extension.getTranslationDir().finalizeValue();
            var dir = extension.getTranslationDir().get().getAsFile().getAbsolutePath();
            config.execute(task);
            task.args(dir); // last arg
        });
    }
}
