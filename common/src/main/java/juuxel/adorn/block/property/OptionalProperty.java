package juuxel.adorn.block.property;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class OptionalProperty<T extends Enum<T> & StringRepresentable> extends Property<OptionalProperty.Value<T>> {
    private static final String NONE_NAME = "none";

    private final EnumProperty<T> delegate;
    private final Value.None<T> none = new Value.None<>();
    private final Map<@Nullable T, Value<T>> values;
    private final List<Value<T>> boxedValues;

    @SuppressWarnings("unchecked")
    public OptionalProperty(EnumProperty<T> delegate) {
        super(delegate.getName(), (Class<Value<T>>) (Class<?>) Value.class);
        this.delegate = delegate;

        values = new LinkedHashMap<>();
        values.put(null, none);
        for (T value : delegate.getPossibleValues()) {
            if (NONE_NAME.equals(value.getSerializedName())) {
                throw new IllegalArgumentException("Delegate has a 'none' value");
            }

            values.put(value, new Value.Some<>(value));
        }
        boxedValues = List.copyOf(values.values());
    }

    @Override
    public Optional<Value<T>> getValue(String name) {
        return NONE_NAME.equals(name) ? Optional.of(none) : delegate.getValue(name).map(values::get);
    }

    @Override
    public List<Value<T>> getPossibleValues() {
        return boxedValues;
    }

    @Override
    public int getInternalIndex(Value<T> value) {
        return boxedValues.indexOf(value);
    }

    @Override
    public String getName(Value<T> value) {
        return value instanceof Value.Some<T>(T inner) ? inner.getSerializedName() : NONE_NAME;
    }

    public EnumProperty<T> getDelegate() {
        return delegate;
    }

    public Value.None<T> getNone() {
        return none;
    }

    public @Nullable Value<T> wrap(@Nullable T value) {
        return values.get(value);
    }

    public Value<T> wrapOrNone(@Nullable T value) {
        return values.getOrDefault(value, none);
    }

    public sealed interface Value<T> extends Comparable<Value<T>> {
        boolean isPresent();
        @Nullable T value();

        record Some<T extends Enum<T> & StringRepresentable>(T value) implements Value<T> {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public int compareTo(Value<T> o) {
                return switch (o) {
                    case Some<T>(var otherValue) -> value.compareTo(otherValue);
                    case None<T> none -> 1;
                };
            }
        }

        final class None<T extends Enum<T> & StringRepresentable> implements Value<T> {
            @Override
            public @Nullable T value() {
                return null;
            }

            @Override
            public boolean isPresent() {
                return false;
            }

            @Override
            public int compareTo(Value<T> o) {
                return o instanceof None<T> ? 0 : -1;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof None<?>;
            }
        }
    }
}
