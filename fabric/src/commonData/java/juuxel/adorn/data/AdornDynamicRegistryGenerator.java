package juuxel.adorn.data;

import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public final class AdornDynamicRegistryGenerator extends FabricDynamicRegistryProvider {
    public AdornDynamicRegistryGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(AdornRegistryKeys.CONE_VARIANT));
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
