package juuxel.adorn.lib.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BlockRegistrar extends KeyedRegistrar<Block> {
    @Override
    <T extends Block> RegisteredBlock<T> register(String id, Supplier<? extends T> provider);

    @Override
    <T extends Block> RegisteredBlock<T> register(String id, Function<? super ResourceKey<Block>, ? extends T> provider);
}
