package juuxel.adorn.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import juuxel.adorn.datagen.GeneratorConfig;
import juuxel.adorn.datagen.GeneratorConfigLoader;
import juuxel.adorn.datagen.Overlay;
import net.minecraft.data.DataOutput;
import net.minecraft.data.MetadataProvider;
import net.minecraft.text.Text;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;
import java.util.stream.Stream;

public final class PackMcmetaGeneration {
    private static final String FABRIC_CONFIG_DIRS_PROPERTY = "adorn.data.fabricConfigDirs";
    private static final String NEOFORGE_CONFIG_DIRS_PROPERTY = "adorn.data.neoforgeConfigDirs";

    public static MetadataProvider create(DataOutput output) {
        var provider = MetadataProvider.create(output, Text.literal("Adorn resources."));
        provider.metadata.put("fabric:overlays", PackMcmetaGeneration::generateFabricOverlays);
        provider.metadata.put("neoforge:overlays", PackMcmetaGeneration::generateNeoForgeOverlays);
        return provider;
    }

    private static Stream<GeneratorConfig> readConfigs(String systemProperty) {
        return AdornTagGenerator.getDataConfigs(systemProperty)
            .stream()
            .map(path -> {
                try {
                    return GeneratorConfigLoader.read(path);
                } catch (IOException e) {
                    throw new UncheckedIOException("Could not read generator configs", e);
                }
            });
    }

    private static JsonElement generateFabricOverlays() {
        var configs = readConfigs(FABRIC_CONFIG_DIRS_PROPERTY);
        return generateOverlays(configs, overlay -> {
            JsonObject condition = new JsonObject();
            condition.addProperty("condition", "fabric:all_mods_loaded");
            var values = new JsonArray();
            values.add(overlay.modId());
            condition.add("values", values);

            JsonObject entry = new JsonObject();
            entry.addProperty("directory", overlay.directory());
            entry.add("condition", condition);
            return entry;
        });
    }

    private static JsonElement generateNeoForgeOverlays() {
        var configs = readConfigs(NEOFORGE_CONFIG_DIRS_PROPERTY);
        return generateOverlays(configs, overlay -> {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "neoforge:mod_loaded");
            condition.addProperty("modid", overlay.modId());
            JsonArray conditions = new JsonArray();
            conditions.add(condition);

            JsonArray formats = new JsonArray();
            formats.add(0);
            formats.add(Integer.MAX_VALUE);

            JsonObject entry = new JsonObject();
            entry.addProperty("directory", overlay.directory());
            entry.add("formats", formats);
            entry.add("neoforge:conditions", conditions);
            return entry;
        });
    }

    private static JsonElement generateOverlays(Stream<GeneratorConfig> configs, Function<Overlay, JsonElement> overlayToJson) {
        JsonArray entriesArray = new JsonArray();

        configs.forEach(config -> {
            if (config.overlay() != null) {
                entriesArray.add(overlayToJson.apply(config.overlay()));
            }
        });

        JsonObject result = new JsonObject();
        result.add("entries", entriesArray);
        return result;
    }
}
