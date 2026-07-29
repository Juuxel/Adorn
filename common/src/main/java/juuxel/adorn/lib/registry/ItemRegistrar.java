package juuxel.adorn.lib.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ItemRegistrar extends KeyedRegistrar<Item> {
    @Override
    <T extends Item> RegisteredItem<T> register(String id, Supplier<? extends T> provider);

    @Override
    <T extends Item> RegisteredItem<T> register(String id, Function<? super ResourceKey<Item>, ? extends T> provider);
}
