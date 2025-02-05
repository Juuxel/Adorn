package juuxel.adorn.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.DeserializationException;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Logging;
import juuxel.adorn.util.Services;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@InlineServices
public abstract class ConfigManager {
    private static final Jankson JANKSON = Jankson.builder()
        .registerSerializer(FluidUnit.class, (unit, m) -> new JsonPrimitive(unit.getId()))
        .registerDeserializer(JsonPrimitive.class, FluidUnit.class,
            (json, m) -> Objects.requireNonNullElse(FluidUnit.byId(json.asString()), FluidUnit.LITRE))
        .build();
    private static final JsonObject DEFAULT = (JsonObject) JANKSON.toJson(new Config());
    private static final Logger LOGGER = Logging.logger();

    private Config config;
    private boolean saveScheduled = false;
    private boolean finalized = false;

    @InlineServices.Getter
    public static ConfigManager get() {
        return Services.load(ConfigManager.class);
    }

    public static Config config() {
        return get().config;
    }

    public static boolean isCompatEnabled(String modId) {
        var compatMap = ConfigManager.config().compat;

        if (!compatMap.containsKey(modId)) {
            compatMap.put(modId, true);
            ConfigManager.get().save();
            return true;
        }

        return compatMap.get(modId);
    }

    protected abstract Path getConfigDirectory();

    private Path getConfigPath() {
        return getConfigDirectory().resolve("Adorn.json5");
    }

    public void init() {
        loadConfig();
    }

    private void loadConfig() {
        var configPath = getConfigPath();
        if (Files.notExists(configPath)) {
            save(new Config());
        }

        try {
            var obj = JANKSON.load(Files.readString(configPath));
            Config config;
            try {
                config = JANKSON.fromJsonCarefully(obj, Config.class);
            } catch (DeserializationException e) {
                // Try deserializing carelessly and throw the exception if it returns null
                config = JANKSON.fromJson(obj, Config.class);
                if (config == null) throw e;
            }

            if (isMissingKeys(obj, DEFAULT)) {
                LOGGER.info("[Adorn] Upgrading config...");
                save(config);
            }

            this.config = config;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Adorn config file!", e);
        }
    }

    public void save() {
        if (finalized) {
            save(config);
        } else {
            saveScheduled = true;
        }
    }

    public void finish() {
        finalized = true;
        if (saveScheduled) {
            save();
        }
    }

    private void save(Config config) {
        try {
            Files.writeString(getConfigPath(), JANKSON.toJson(config).toJson(true, true));
        } catch (IOException e) {
            LOGGER.error("[Adorn] Could not save config file {}", getConfigPath(), e);
        }
    }

    private boolean isMissingKeys(JsonObject config, JsonObject defaults) {
        for (var entry : defaults.entrySet()) {
            if (!config.containsKey(entry.getKey())) return true;

            if (entry.getValue() instanceof JsonObject value) {
                var actual = config.get(JsonObject.class, entry.getKey());
                return actual == null || isMissingKeys(actual, value);
            }
        }

        return false;
    }
}
