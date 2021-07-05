package juuxel.adorn.compat.dashloader;

import juuxel.adorn.block.property.OptionalProperty;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.blockstate.property.value.DashPropertyValue;

@DashObject(OptionalProperty.Value.None.class)
public class DashOptionalNoneValue implements DashPropertyValue {
    @DashConstructor(ConstructorMode.EMPTY)
    public DashOptionalNoneValue() {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Comparable<?> toUndash(DashRegistry registry) {
        return new OptionalProperty.Value.None();
    }
}
