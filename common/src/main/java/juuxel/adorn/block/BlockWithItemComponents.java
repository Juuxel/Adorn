package juuxel.adorn.block;

import juuxel.adorn.lib.registry.Registered;
import net.minecraft.component.ComponentType;

public interface BlockWithItemComponents {
    void addItemComponents(ComponentConsumer consumer);

    @FunctionalInterface
    interface ComponentConsumer {
        <T> void add(ComponentType<T> type, T component);

        default <T> void add(Registered<ComponentType<T>> type, T component) {
            add(type.get(), component);
        }
    }
}
