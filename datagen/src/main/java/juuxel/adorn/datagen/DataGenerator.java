package juuxel.adorn.datagen;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DataGenerator {
    private final List<Path> configFiles;
    private final DataOutput mainDataOutput;
    private final DependencyOutput dependencyOutput;

    private DataGenerator(List<Path> configFiles, DataOutput mainDataOutput, DependencyOutput dependencyOutput) {
        if (mainDataOutput == null && dependencyOutput == null) {
            throw new IllegalArgumentException("Must generate at least data or dependencies");
        }

        this.configFiles = configFiles;
        this.mainDataOutput = mainDataOutput;
        this.dependencyOutput = dependencyOutput;
    }

    public static void generate(List<Path> configFiles, DataOutput output) {
        builder(configFiles).generateData(output).build().generate();
    }

    public void generate() {
        var cache = new TemplateCache();

        for (Path configFile : configFiles) {
            GeneratorConfig config;
            try {
                config = GeneratorConfigLoader.read(configFile);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read generator config from " + configFile, e);
            }
            generate(config, cache);
        }
    }

    private void generate(GeneratorConfig config, TemplateCache cache) {
        var output = mainDataOutput;

        if (output != null && config.overlay() != null) {
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

    private <M extends Material> void generate(DataOutput dataOutput, List<Generator> gens, Iterable<GeneratorConfig.MaterialEntry<M>> mats, TemplateCache templateCache, GeneratorConfig config) {
        var conditionType = config.conditionType();
        for (var gen : gens) {
            var templateText = templateCache.load(gen.templatePath());
            var dependencies = dependencyOutput != null ? templateCache.loadProperties(gen.templateDependenciesPath()) : null;

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

                if (dependencies != null) {
                    String items = dependencies.getProperty("items");
                    String textures = dependencies.getProperty("textures");

                    if (items != null) {
                        for (String item : items.split(", *")){
                            dependencyOutput.acceptItem(Id.parse(TemplateApplier.apply(item, mainSubstitutions)));
                        }
                    }

                    if (textures != null) {
                        for (String texture : textures.split(", *")) {
                            dependencyOutput.acceptTexture(Id.parse(TemplateApplier.apply(texture, mainSubstitutions)));
                        }
                    }
                }

                if (dataOutput == null) continue;

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

    public static Builder builder(List<Path> configFiles) {
        return new Builder(configFiles);
    }

    public static final class Builder {
        private final List<Path> configFiles;
        private DataOutput mainDataOutput;
        private DependencyOutput dependencyOutput;

        private Builder(List<Path> configFiles) {
            this.configFiles = configFiles;
        }

        public Builder generateData(DataOutput output) {
            this.mainDataOutput = output;
            return this;
        }

        public Builder generateDependencies(DependencyOutput output) {
            this.dependencyOutput = output;
            return this;
        }

        public DataGenerator build() {
            return new DataGenerator(configFiles, mainDataOutput, dependencyOutput);
        }
    }

    private static final class TemplateCache {
        private final Map<String, String> cache = new HashMap<>();
        private final Map<String, Properties> propertiesCache = new HashMap<>();

        public String load(String path) {
            return cache.computeIfAbsent(path, p -> {
                try (var in = getClass().getResourceAsStream("/adorn/templates/" + p)) {
                    return new String(in.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        public String loadOptional(String path) {
            String computed = cache.computeIfAbsent(path, p -> {
                try (var in = getClass().getResourceAsStream("/adorn/templates/" + p)) {
                    if (in == null) return "";
                    return new String(in.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

            return !computed.isEmpty() ? computed : null;
        }

        public Properties loadProperties(String path) {
            return propertiesCache.computeIfAbsent(path, p -> {
                var props = new Properties();
                var text = loadOptional(path);

                if (text != null) {
                    try {
                        props.load(new StringReader(text));
                    } catch (IOException e) {
                        // won't happen
                    }
                }

                return props;
            });
        }
    }
}
