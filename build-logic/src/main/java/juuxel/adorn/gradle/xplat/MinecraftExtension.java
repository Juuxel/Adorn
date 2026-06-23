package juuxel.adorn.gradle.xplat;

import org.gradle.api.provider.Property;

public abstract class MinecraftExtension {
    public abstract Property<String> getMinecraftVersion();
}
