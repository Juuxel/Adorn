package juuxel.adorn.gradle;

import juuxel.adorn.gradle.action.ZipTransformerAction;
import juuxel.adorn.gradle.util.zip.MinifyJson;
import juuxel.adorn.gradle.util.zip.ZipTransformer;
import juuxel.adorn.gradle.xplat.HoardExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.api.tasks.bundling.Jar;

public final class HoardPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPlugins().apply(CorePlugin.class);

        var hoardJar = project.getTasks().register("hoardJar", Jar.class, task -> {
            task.setGroup(CorePlugin.TASK_GROUP);
            var baseClassifier = project.getTasks().named("jar", AbstractArchiveTask.class).flatMap(AbstractArchiveTask::getArchiveClassifier);
            task.getArchiveClassifier().convention(baseClassifier.map(c -> c + "-resources"));
            task.doLast(
                new ZipTransformerAction(
                    transformer -> transformer.addStep(new MinifyJson())
                        .compressionMethod(ZipTransformer.CompressionMethod.STORED)
                )
            );
        });

        var extension = CorePlugin.registerExtension(project, "hoard", HoardExtension.class, hoardJar);
    }
}
