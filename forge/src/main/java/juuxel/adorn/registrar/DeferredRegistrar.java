package juuxel.adorn.registrar;

import com.google.common.collect.Iterators;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IHolderExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DeferredRegistrar<T> implements KeyedRegistrar<T>, NeoRegistrar<T> {
    private final DeferredRegister<T> register;
    private final List<DeferredHolder<T, ? extends T>> objects = new ArrayList<>();

    public DeferredRegistrar(ResourceKey<? extends Registry<T>> registry) {
        register = DeferredRegister.create(registry, AdornCommon.NAMESPACE);
    }

    @Override
    public void hook(IEventBus modBus) {
        register.register(modBus);
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Function<? super ResourceKey<T>, ? extends U> provider) {
        var key = ResourceKey.create(register.getRegistryKey(), Identifier.fromNamespaceAndPath(register.getNamespace(), id));
        return register(id, () -> provider.apply(key));
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider) {
        var registryObject = register.register(id, provider);
        objects.add(registryObject);
        return new Registered.WithKey<>() {
            @Override
            public ResourceKey<T> key() {
                return ((IHolderExtension<T>) registryObject).getKey();
            }

            @Override
            public U get() {
                return registryObject.get();
            }

            @Override
            public Holder<T> entry() {
                return registryObject;
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(objects.iterator(), DeferredHolder::get);
    }
}
