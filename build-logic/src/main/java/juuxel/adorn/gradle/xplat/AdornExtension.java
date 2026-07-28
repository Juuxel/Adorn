package juuxel.adorn.gradle.xplat;

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;

import javax.inject.Inject;

public abstract class AdornExtension implements ExtensionAware {
    @Inject
    protected abstract Project getProject();
}
