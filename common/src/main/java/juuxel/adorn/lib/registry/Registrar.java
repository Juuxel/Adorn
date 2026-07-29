package juuxel.adorn.lib.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Registrar<T> extends Iterable<T> {
    /**
     * Registers an object with the id. The object is created using the provider.
     */
    <U extends T> Registered<U> register(String id, Supplier<? extends U> provider);

    @SuppressWarnings("unchecked")
    static <K, U> RegisteredMap<K, U> registerBy(Collection<? extends K> keys, Function<K, Registered<? extends U>> factory) {
        List<? extends K> keyList;
        if (keys instanceof List<? extends K> l) {
            keyList = l;
        } else {
            keyList = List.copyOf(keys);
        }

        RegisteredMap.MapFactory<K> mapFactory;
        if (keyList.getFirst() instanceof Enum<?> e) {
            mapFactory = RegisteredMap.MapFactory.enumKeys(e.getClass());
        } else {
            mapFactory = RegisteredMap.MapFactory.linkedHashKeys();
        }

        RegisteredMap.Builder<K, U> builder = RegisteredMap.builder(mapFactory);
        for (K key : keyList) {
            builder.put(key, factory.apply(key));
        }

        return builder.build();
    }

    static <K, U> RegisteredMap<K, U> registerBy(K[] keys, Function<K, Registered<? extends U>> factory) {
        return registerBy(Arrays.asList(keys), factory);
    }

    static <T, R extends Registered<T>> ColorCollection<R> registerColored(String name, BiFunction<String, DyeColor, R> factory) {
        var names = ColorCollection.prefixWithColor(ColorCollection.create(name));
        return ColorCollection.zipMap(names, ColorCollection.VALUES, factory);
    }

    static <T, R extends Registered<T>> WeatheringCopperCollection<R> registerWeatheringCopper(
        String name,
        BiFunction<String, WeatheringCopper.WeatherState, R> weatheringFactory,
        BiFunction<String, R, R> waxedFactory
    ) {
        var names = WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(name));
        var weathering = WeatheringCopperCollection.zipMap(names.weathering(), WeatheringCopperCollection.STATES, weatheringFactory);
        var waxed = WeatheringCopperCollection.zipMap(names.waxed(), weathering, waxedFactory);
        return new WeatheringCopperCollection<>(weathering, waxed);
    }
}
