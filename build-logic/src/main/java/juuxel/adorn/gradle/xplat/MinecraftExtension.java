package juuxel.adorn.gradle.xplat;

import juuxel.adorn.gradle.CorePlugin;
import juuxel.adorn.gradle.packageinfo.GeneratePackageInfos;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;

import javax.inject.Inject;

public abstract class MinecraftExtension {
    public abstract Property<String> getMinecraftVersion();

    @Inject
    protected abstract Project getProject();

    public void generatePackageInfos(SourceSet sourceSet) {
        var taskName = sourceSet.getTaskName("generate", "PackageInfos");
        var taskProvider = getProject().getTasks().register(taskName, GeneratePackageInfos.class, task -> {
            task.setGroup(CorePlugin.TASK_GROUP);
            var srcDir = getProject().provider(() -> sourceSet.getJava().getSrcDirs().iterator().next());
            task.getSourceRoot().convention(getProject().getLayout().dir(srcDir));
            task.getOutputDirectory().convention(
                getProject()
                    .getLayout()
                    .getBuildDirectory()
                    .dir(taskName + "Sources")
            );
        });
        sourceSet.getJava().srcDir(taskProvider.flatMap(GeneratePackageInfos::getOutputDirectory));
    }
}
