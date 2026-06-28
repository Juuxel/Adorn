package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.MappingBridge;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public final class MappingBridgeFabric implements MappingBridge {
    private final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

    @Override
    public String remapToRuntime(String className) {
        return mappingResolver.mapClassName("intermediary", className);
    }
}
