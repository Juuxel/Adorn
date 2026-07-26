package juuxel.adorn.lib.registry;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.function.Supplier;

public final class TrackedDataHandlerRegistrar extends AbstractRegistrar<EntityDataSerializer<?>> {
    @Override
    public <U extends EntityDataSerializer<?>> Registered<U> register(String id, Supplier<? extends U> provider) {
        var value = provider.get();
        FabricTrackedDataRegistry.register(AdornCommon.id(id), value);
        objects.add(value);
        return () -> value;
    }
}
