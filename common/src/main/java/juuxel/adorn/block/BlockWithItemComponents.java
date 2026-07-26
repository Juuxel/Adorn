package juuxel.adorn.block;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.core.component.DataComponentType;

public interface BlockWithItemComponents {
    void addItemComponents(ComponentConsumer consumer);

    @FunctionalInterface
    interface ComponentConsumer {
        <T> void add(DataComponentType<T> type, T component);

        default <T> void add(Registered<DataComponentType<T>> type, T component) {
            add(type.get(), component);
        }
    }
}
