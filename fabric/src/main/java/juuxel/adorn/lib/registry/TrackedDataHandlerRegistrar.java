package juuxel.adorn.lib.registry;

import juuxel.adorn.AdornCommon;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;

import java.util.function.Supplier;

public final class TrackedDataHandlerRegistrar extends AbstractRegistrar<TrackedDataHandler<?>> {
    @Override
    public <U extends TrackedDataHandler<?>> Registered<U> register(String id, Supplier<? extends U> provider) {
        var value = provider.get();
        FabricTrackedDataRegistry.register(AdornCommon.id(id), value);
        objects.add(value);
        return () -> value;
    }
}
