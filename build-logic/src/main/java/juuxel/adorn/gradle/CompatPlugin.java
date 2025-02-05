package juuxel.adorn.gradle;

import juuxel.adorn.gradle.compat.CompatExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Map;

public final class CompatPlugin implements Plugin<Project> {
    public static final String FABRIC_COMPAT_ARTIFACTS_CONFIGURATION = "fabricCompatArtifacts";
    public static final String NEOFORGE_COMPAT_ARTIFACTS_CONFIGURATION = "neoForgeCompatArtifacts";

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(ModularDataGeneratorPlugin.class);
        project.getConfigurations().consumable(FABRIC_COMPAT_ARTIFACTS_CONFIGURATION);
        project.getConfigurations().consumable(NEOFORGE_COMPAT_ARTIFACTS_CONFIGURATION);
        project.getExtensions().create("compat", CompatExtension.class, project);

        addJijDependency(project.project(":fabric"), project, FABRIC_COMPAT_ARTIFACTS_CONFIGURATION);
        addJijDependency(project.project(":forge"), project, NEOFORGE_COMPAT_ARTIFACTS_CONFIGURATION);
    }

    private static void addJijDependency(Project target, Project owner, String configuration) {
        var deps = target.getDependencies();
        deps.add("include", deps.project(Map.of("path", owner.getPath(), "configuration", configuration)));
    }
}
