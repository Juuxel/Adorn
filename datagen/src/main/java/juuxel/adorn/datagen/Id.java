package juuxel.adorn.datagen;

import java.util.regex.Pattern;

/**
 * A namespaced ID. Mimics {@code net.minecraft.resources.Identifier}.
 */
public record Id(String namespace, String path) {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^[a-z0-9._-]+$");
    private static final Pattern PATH_PATTERN = Pattern.compile("^[a-z0-9._/-]+$");

    public Id {
        if (!NAMESPACE_PATTERN.matcher(namespace).matches()) {
            throw new IllegalArgumentException(
                "ID namespace '%s' doesn't match %s".formatted(namespace, NAMESPACE_PATTERN)
            );
        } else if (!PATH_PATTERN.matcher(path).matches()) {
            throw new IllegalArgumentException(
                "ID path '%s' doesn't match %s".formatted(path, PATH_PATTERN)
            );
        }
    }

    public Id rawSuffixed(String suffix) {
        return new Id(namespace, path + suffix);
    }

    public Id suffixed(String suffix) {
        return rawSuffixed("_" + suffix);
    }

    public Id rawPrefixed(String prefix) {
        return new Id(namespace, prefix + path);
    }

    public Id prefixed(String prefix) {
        return rawPrefixed(prefix + "_");
    }

    @Override
    public String toString() {
        return namespace + ':' + path;
    }

    public static Id parse(String id) {
        int idIndex = id.indexOf(':');
        if (idIndex < 0) {
            throw new IllegalArgumentException("Id '%s' does not contain colon (:)!".formatted(id));
        }
        var ns = id.substring(0, idIndex);
        var path = id.substring(idIndex + 1);
        return new Id(ns, path);
    }
}
