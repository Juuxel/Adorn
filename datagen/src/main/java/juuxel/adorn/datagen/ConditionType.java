package juuxel.adorn.datagen;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ConditionType {
    NONE("none", null, null, Map.of()),
    FABRIC("fabric", null, null, Map.of("load-condition", "fabric-conditions.json", "model_condition", "fabric-model-conditions.json")),
    FORGE("forge", null, null, Map.of("load-condition", "forge-conditions.json", "model_condition", "forge-model-conditions.json")),
    ;

    private static final Map<String, ConditionType> BY_ID = Arrays.stream(values())
        .collect(Collectors.toMap(type -> type.id, Function.identity()));

    private final String id;
    private final String separateFilePathTemplate;
    private final String separateFileTemplatePath;
    private final Map<String, String> conditionsInFileTemplatePathsByType;

    ConditionType(String id, String separateFilePathTemplate, String separateFileTemplatePath, Map<String, String> conditionsInFileTemplatePathsByType) {
        this.id = id;
        this.separateFilePathTemplate = separateFilePathTemplate;
        this.separateFileTemplatePath = separateFileTemplatePath;
        this.conditionsInFileTemplatePathsByType = conditionsInFileTemplatePathsByType;
    }

    public String getSeparateFilePathTemplate() {
        return separateFilePathTemplate;
    }

    public String getSeparateFileTemplatePath() {
        return separateFileTemplatePath;
    }

    public Map<String, String> getConditionsInFileTemplatePathsByType() {
        return conditionsInFileTemplatePathsByType;
    }

    public static ConditionType parse(String id) {
        return id.isEmpty() ? NONE : BY_ID.get(id);
    }
}
