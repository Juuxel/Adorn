package juuxel.adorn.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.ArrayList;
import java.util.List;

public final class TextBuilder {
    private final List<Component> parts = new ArrayList<>();

    public TextBuilder add(Component text) {
        parts.add(text);
        return this;
    }

    public TextBuilder newLine() {
        parts.add(Component.literal("\n"));
        return this;
    }

    public MutableComponent build() {
        return ComponentSerialization.createFromList(parts);
    }
}
