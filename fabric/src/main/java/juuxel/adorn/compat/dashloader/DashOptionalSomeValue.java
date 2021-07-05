package juuxel.adorn.compat.dashloader;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import juuxel.adorn.block.property.OptionalProperty;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.blockstate.property.value.DashPropertyValue;

@DashObject(OptionalProperty.Value.Some.class)
public class DashOptionalSomeValue implements DashPropertyValue {
    @Serialize(order = 0)
    public final String name;
    @Serialize(order = 1)
    public final String enumClass;

    public DashOptionalSomeValue(@Deserialize("name") String name, @Deserialize("enumClass") String enumClass) {
        this.name = name;
        this.enumClass = enumClass;
    }

    @DashConstructor(ConstructorMode.OBJECT)
    public DashOptionalSomeValue(OptionalProperty.Value.Some<?> value) {
        this(value.getValue().name(), value.getValue().getClass().getName());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Comparable<?> toUndash(DashRegistry registry) {
        try {
            Class c = Class.forName(enumClass);
            Enum<?> e = Enum.valueOf(c, name);
            return new OptionalProperty.Value.Some(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
