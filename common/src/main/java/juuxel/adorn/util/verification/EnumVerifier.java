package juuxel.adorn.util.verification;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.platform.MappingBridge;
import juuxel.adorn.platform.ModBridge;
import juuxel.adorn.util.Logging;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class EnumVerifier {
    private static final Logger LOGGER = Logging.logger();
    private static final List<Violation> violations = new ArrayList<>();

    public static List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    @SuppressWarnings("unchecked")
    public static void verifyEnums() {
        if (!ConfigManager.config().checkModCompatIssues) return;

        try {
            byte[] dataBytes = ModBridge.get().readModFile("enum_verification_data.json");

            if (dataBytes == null) {
                LOGGER.error("[Adorn] Enum verification data not present");
                return;
            }

            var type = new TypeToken<Map<String, Set<String>>>() {};
            var data = new Gson().fromJson(new String(dataBytes, StandardCharsets.UTF_8), type);

            for (String className : data.keySet()) {
                Set<String> expected = data.get(className);
                className = MappingBridge.get().remapToRuntime(className);
                Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) Class.forName(className);
                Enum<?>[] values = enumClass.getEnumConstants();

                if (expected.size() != values.length) {
                    Set<String> found = Arrays.stream(values).map(Enum::name).collect(Collectors.toSet());
                    List<String> extra = Sets.difference(found, expected).stream().sorted().toList();
                    violations.add(new Violation(enumClass, extra));
                }
            }

            if (!violations.isEmpty()) {
                var sb = new StringBuilder("[Adorn] Found the following enum violations:");

                for (Violation violation : violations) {
                    sb.append("\n - ")
                        .append(violation.enumClass.getSimpleName())
                        .append(" has extra entries ")
                        .append(violation.additionalEntries);
                }

                LOGGER.error(sb.toString());
            }
        } catch (IOException | ReflectiveOperationException e) {
            LOGGER.error("[Adorn] Could not verify enums", e);
        }
    }

    public record Violation(Class<?> enumClass, List<String> additionalEntries) {
    }
}
