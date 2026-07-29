package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AbstractRegistrar;
import juuxel.adorn.lib.registry.BlockRegistrar;
import juuxel.adorn.lib.registry.ItemRegistrar;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredBlock;
import juuxel.adorn.lib.registry.RegisteredItem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public class RegistrarImpl<T> extends AbstractRegistrar<T> implements KeyedRegistrar<T> {
    private final Registry<T> registry;

    public RegistrarImpl(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider) {
        var key = createKey(id);
        U value = provider.get();
        return register(key, value);
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Function<? super ResourceKey<T>, ? extends U> provider) {
        var key = createKey(id);
        U value = provider.apply(key);
        return register(key, value);
    }

    private <U extends T> Registered.WithKey<T, U> register(ResourceKey<T> key, U value) {
        Holder.Reference<T> entry = Registry.registerForHolder(registry, key, value);
        objects.add(value);
        return createSupplier(key, value, entry);
    }

    protected <U extends T> Registered.WithKey<T, U> createSupplier(ResourceKey<T> key, U value, Holder<T> holder) {
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return value;
            }

            @Override
            public ResourceKey<T> key() {
                return key;
            }

            @Override
            public Holder<T> entry() {
                return holder;
            }
        };
    }

    private ResourceKey<T> createKey(String id) {
        return ResourceKey.create(registry.key(), AdornCommon.id(id));
    }

    @SuppressWarnings("unchecked")
    public static final class BlockImpl extends RegistrarImpl<Block> implements BlockRegistrar {
        public BlockImpl() {
            super(BuiltInRegistries.BLOCK);
        }

        @Override
        public <U extends Block> RegisteredBlock<U> register(String id, Supplier<? extends U> provider) {
            return (RegisteredBlock<U>) super.register(id, provider);
        }

        @Override
        public <U extends Block> RegisteredBlock<U> register(String id, Function<? super ResourceKey<Block>, ? extends U> provider) {
            return (RegisteredBlock<U>) super.register(id, provider);
        }

        @Override
        protected <U extends Block> Registered.WithKey<Block, U> createSupplier(ResourceKey<Block> key, U value, Holder<Block> holder) {
            return new RegisteredBlock<>() {
                @Override
                public U get() {
                    return value;
                }

                @Override
                public ResourceKey<Block> key() {
                    return key;
                }

                @Override
                public Holder<Block> entry() {
                    return holder;
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    public static final class ItemImpl extends RegistrarImpl<Item> implements ItemRegistrar {
        public ItemImpl() {
            super(BuiltInRegistries.ITEM);
        }

        @Override
        public <U extends Item> RegisteredItem<U> register(String id, Supplier<? extends U> provider) {
            return (RegisteredItem<U>) super.register(id, provider);
        }

        @Override
        public <U extends Item> RegisteredItem<U> register(String id, Function<? super ResourceKey<Item>, ? extends U> provider) {
            return (RegisteredItem<U>) super.register(id, provider);
        }

        @Override
        protected <U extends Item> Registered.WithKey<Item, U> createSupplier(ResourceKey<Item> key, U value, Holder<Item> holder) {
            return new RegisteredItem<>() {
                @Override
                public U get() {
                    return value;
                }

                @Override
                public ResourceKey<Item> key() {
                    return key;
                }

                @Override
                public Holder<Item> entry() {
                    return holder;
                }
            };
        }
    }
}
