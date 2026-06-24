package juuxel.adorn.gradle.datagen;

import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;

import javax.inject.Inject;

public abstract class DataGeneratorExtension {
    private final NamedDomainObjectContainer<Settings> settings;

    @Inject
    public DataGeneratorExtension(Project project) {
        settings = project.container(Settings.class);
    }

    public NamedDomainObjectContainer<Settings> getSettings() {
        return settings;
    }

    public void settings(Action<NamedDomainObjectContainer<Settings>> action) {
        action.execute(settings);
    }

    public static abstract class Settings implements Named {
        private final NamedDomainObjectContainer<DataConfig> configs;

        @Inject
        public Settings(Project project) {
            configs = project.container(DataConfig.class);
            getGeneratedSources().convention(getSourceSet().flatMap(sourceSet -> project.getLayout().getBuildDirectory().dir("%sDataGeneratedSources".formatted(sourceSet.getName()))));
            getGeneratedResources().convention(getSourceSet().map(sourceSet -> project.getLayout().getProjectDirectory().dir("src/%s/generatedResources".formatted(sourceSet.getName()))));
            getGenerateTags().convention(false);
            getGenerateEmiFiles().convention(true);
            getIncludeCommonFilesInEmi().convention(false);
            getGenerateCode().convention(true);
        }

        public abstract Property<SourceSet> getSourceSet();
        public abstract DirectoryProperty getGeneratedSources();
        public abstract DirectoryProperty getGeneratedResources();
        public abstract Property<String> getModId();
        public abstract Property<Boolean> getGenerateTags();
        public abstract Property<Boolean> getGenerateCode();

        public abstract Property<Boolean> getGenerateEmiFiles();
        public abstract Property<Boolean> getIncludeCommonFilesInEmi();

        public NamedDomainObjectContainer<DataConfig> getConfigs() {
            return configs;
        }

        public void configs(Action<NamedDomainObjectContainer<DataConfig>> action) {
            action.execute(configs);
        }
    }
}
