package juuxel.adorn.compat.dashloader;

import io.activej.serializer.annotations.Deserialize;
import io.activej.serializer.annotations.Serialize;
import juuxel.adorn.block.property.OptionalProperty;
import net.minecraft.state.property.Property;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.blockstate.property.DashEnumProperty;
import net.oskarstrom.dashloader.blockstate.property.DashProperty;

@DashObject(OptionalProperty.class)
public class DashOptionalProperty implements DashProperty {
    @Serialize(order = 0)
    public final DashEnumProperty property;

    public DashOptionalProperty(@Deserialize("property") DashEnumProperty property) {
        this.property = property;
    }

    @DashConstructor(ConstructorMode.OBJECT)
    public DashOptionalProperty(OptionalProperty<?> property) {
        this(new DashEnumProperty(property.getDelegate()));
    }

    @Override
    public Property<?> toUndash(DashRegistry registry) {
        return new OptionalProperty<>(property.toUndash(registry));
    }
}
