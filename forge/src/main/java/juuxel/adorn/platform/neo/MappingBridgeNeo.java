package juuxel.adorn.platform.neo;

import juuxel.adorn.platform.MappingBridge;

public final class MappingBridgeNeo implements MappingBridge {
    @Override
    public String remapToRuntime(String className) {
        return className;
    }
}
