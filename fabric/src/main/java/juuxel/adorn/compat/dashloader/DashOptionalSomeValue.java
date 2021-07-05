package juuxel.adorn.compat.dashloader;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import juuxel.adorn.block.property.OptionalProperty;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.ExtraVariables;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.blockstate.property.value.DashEnumValue;
import net.oskarstrom.dashloader.blockstate.property.value.DashPropertyValue;

@DashObject(OptionalProperty.Value.Some.class)
public class DashOptionalSomeValue implements DashPropertyValue {
    @Serialize(order = 0)
    public final DashEnumValue value;

    public DashOptionalSomeValue(@Deserialize("value") DashEnumValue value) {
        this.value = value;
    }

    @DashConstructor(ConstructorMode.OBJECT_EXTRA)
    public DashOptionalSomeValue(OptionalProperty.Value.Some<?> value, ExtraVariables extra) {
        this(new DashEnumValue(value.getValue(), extra));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Comparable<?> toUndash(DashRegistry registry) {
        return new OptionalProperty.Value.Some(value.toUndash(registry));
    }
}
