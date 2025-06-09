package juuxel.adorn.data;

import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public final class AdornDynamicRegistryGenerator extends FabricDynamicRegistryProvider {
    public AdornDynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(AdornRegistryKeys.CONE_VARIANT));
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
