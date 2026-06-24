package juuxel.adorn.datagen;

import java.util.Map;
import java.util.Set;

public record GeneratorConfig(
    Set<MaterialEntry<WoodMaterial>> woods,
    Set<MaterialEntry<StoneMaterial>> stones,
    Set<MaterialEntry<ColorMaterial>> colors,
    ConditionType conditionType,
    Map<String, String> rootReplacements,
    Overlay overlay,
    String className
) {
    public record MaterialEntry<M extends Material>(
        M material,
        Set<String> exclude,
        Map<String, String> replace
    ) {
    }
}
