package juuxel.adorn.lib.registry;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;

@InlineServices
public interface RegistrarFactory {
    <T> KeyedRegistrar<T> create(ResourceKey<Registry<T>> registryKey);

    BlockRegistrar createBlocks();
    ItemRegistrar createItems();

    Registrar<EntityDataSerializer<?>> createForTrackedDataHandlers();

    @InlineServices.Getter
    static RegistrarFactory get() {
        return Services.load(RegistrarFactory.class);
    }
}
