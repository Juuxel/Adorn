package juuxel.adorn.lib.registry;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

@InlineServices
public interface RegistrarFactory {
    <T> KeyedRegistrar<T> create(RegistryKey<Registry<T>> registryKey);

    @InlineServices.Getter
    static RegistrarFactory get() {
        return Services.load(RegistrarFactory.class);
    }
}
