package juuxel.adorn.registrar;

import com.google.common.collect.Iterators;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.BlockRegistrar;
import juuxel.adorn.lib.registry.ItemRegistrar;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredBlock;
import juuxel.adorn.lib.registry.RegisteredItem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistrar<T> implements KeyedRegistrar<T>, NeoRegistrar<T> {
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
        return createSupplier(registryObject);
    }

    protected <U extends T> Registered.WithKey<T, U> createSupplier(DeferredHolder<T, ? extends U> holder) {
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return holder.get();
            }

            @Override
            public ResourceKey<T> key() {
                return holder.getKey();
            }

            @Override
            public Holder<T> entry() {
                return holder;
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(objects.iterator(), DeferredHolder::get);
    }

    @SuppressWarnings("unchecked")
    public static final class BlockImpl extends DeferredRegistrar<Block> implements BlockRegistrar {
        public BlockImpl() {
            super(Registries.BLOCK);
        }

        @Override
        public <U extends Block> RegisteredBlock<U> register(String id, Function<? super ResourceKey<Block>, ? extends U> provider) {
            return (RegisteredBlock<U>) super.register(id, provider);
        }

        @Override
        public <U extends Block> RegisteredBlock<U> register(String id, Supplier<? extends U> provider) {
            return (RegisteredBlock<U>) super.register(id, provider);
        }

        @Override
        protected <U extends Block> Registered.WithKey<Block, U> createSupplier(DeferredHolder<Block, ? extends U> holder) {
            return new RegisteredBlock<>() {
                @Override
                public U get() {
                    return holder.get();
                }

                @Override
                public ResourceKey<Block> key() {
                    return holder.getKey();
                }

                @Override
                public Holder<Block> entry() {
                    return holder;
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    public static final class ItemImpl extends DeferredRegistrar<Item> implements ItemRegistrar {
        public ItemImpl() {
            super(Registries.ITEM);
        }

        @Override
        public <U extends Item> RegisteredItem<U> register(String id, Function<? super ResourceKey<Item>, ? extends U> provider) {
            return (RegisteredItem<U>) super.register(id, provider);
        }

        @Override
        public <U extends Item> RegisteredItem<U> register(String id, Supplier<? extends U> provider) {
            return (RegisteredItem<U>) super.register(id, provider);
        }

        @Override
        protected <U extends Item> Registered.WithKey<Item, U> createSupplier(DeferredHolder<Item, ? extends U> holder) {
            return new RegisteredItem<>() {
                @Override
                public U get() {
                    return holder.get();
                }

                @Override
                public ResourceKey<Item> key() {
                    return holder.getKey();
                }

                @Override
                public Holder<Item> entry() {
                    return holder;
                }
            };
        }
    }
}
