package juuxel.adorn;

import net.minecraft.resources.Identifier;

public final class AdornCommon {
    public static final String NAMESPACE = "adorn";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}
