package juuxel.adorn.datagen;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataGenerator {
    public static void generate(List<Path> configFiles, DataOutput output) {
        var cache = new TemplateCache();

        for (Path configFile : configFiles) {
            GeneratorConfig config;
            try {
                config = GeneratorConfigLoader.read(configFile);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read generator config from " + configFile, e);
            }
            generate(output, config, cache);
        }
    }

    private static void generate(DataOutput output, GeneratorConfig config, TemplateCache cache) {
        if (config.overlay() != null) {
            output = new OverlayedDataOutput(output, config.overlay());
        }

        var stoneMaterials = config.stones();
        generate(output, Generator.STONE_GENERATORS, stoneMaterials, cache, config);
        generate(output, Generator.SIDED_STONE_GENERATORS, stoneMaterials.stream().filter(entry -> entry.material().getHasSidedTexture()).toList(), cache, config);
        generate(
            output,
            Generator.UNSIDED_STONE_GENERATORS,
            stoneMaterials.stream().filter(entry -> !entry.material().getHasSidedTexture()).toList(),
            cache,
            config
        );
        generate(output, Generator.WOOD_GENERATORS, config.woods(), cache, config);
        generate(output, Generator.WOOD_GENERATORS, config.colors(), cache, config);
        generate(output, Generator.WOOL_GENERATORS, config.colors(), cache, config);
    }

    private static <M extends Material> void generate(DataOutput dataOutput, List<Generator> gens, Iterable<GeneratorConfig.MaterialEntry<M>> mats, TemplateCache templateCache, GeneratorConfig config) {
        var conditionType = config.conditionType();
        for (var gen : gens) {
            var templateText = templateCache.load(gen.templatePath());

            for (var entry : mats) {
                if (entry.exclude().contains(gen.id())) continue;
                var mat = entry.material();
                var mainSubstitutions = TemplateContext.buildSubstitutions(it -> {
                    it.set("wood_texture_separator", "_");
                    it.set("advancement-condition", "<load-condition>");
                    it.set("loot-table-condition", "<load-condition>");
                    it.set("recipe-condition", "<load-condition>");
                    it.set("load-condition", "");
                    it.set("model_condition", "");

                    conditionType.getConditionsInFileTemplatePathsByType().forEach((type, path) -> {
                        it.set(type, templateCache.load(path));
                    });

                    it.putAll(config.rootReplacements());
                    it.init(mat);
                    it.putAll(entry.replace());
                });
                var output = TemplateApplier.apply(templateText, mainSubstitutions);
                var filePathStr = TemplateApplier.apply(gen.outputPathTemplate(), mainSubstitutions);
                dataOutput.write(filePathStr, output);

                if (gen.requiresCondition() && mat.isModded()) {
                    var externalConditionPathTemplate = conditionType.getSeparateFilePathTemplate();
                    if (externalConditionPathTemplate != null) {
                        var conditionTemplate = templateCache.load(conditionType.getSeparateFileTemplatePath());
                        var conditionSubstitutions = TemplateContext.buildSubstitutions(it -> {
                            it.set("mod-id", mat.getId().namespace());
                            it.set("file-path", filePathStr);
                        });
                        var conditionText = TemplateApplier.apply(conditionTemplate, conditionSubstitutions);
                        var conditionPathStr = TemplateApplier.apply(externalConditionPathTemplate, conditionSubstitutions);
                        dataOutput.write(conditionPathStr, conditionText);
                    }
                }
            }
        }
    }

    private static final class TemplateCache {
        private final Map<String, String> cache = new HashMap<>();

        public String load(String path) {
            return cache.computeIfAbsent(path, p -> {
                try (var in = getClass().getResourceAsStream("/adorn/templates/" + p)) {
                    return new String(in.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }
}
