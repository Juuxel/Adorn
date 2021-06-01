package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.Registered;
import juuxel.adorn.platform.Registrar;
import kotlin.jvm.functions.Function0;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public final class RegistrarImpl<T> implements Registrar<T> {
    private final Registry<T> registry;

    private RegistrarImpl(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull <U extends T> Registered<U> register(String id, Function0<U> provider) {
        U registered = Registry.register(registry, AdornCommon.id(id), provider.invoke());
        return Registered.of(() -> registered);
    }

    public static Registrar<Block> block() {
        return new RegistrarImpl<>(Registry.BLOCK);
    }

    public static Registrar<Item> item() {
        return new RegistrarImpl<>(Registry.ITEM);
    }

    public static Registrar<BlockEntityType<?>> blockEntity() {
        return new RegistrarImpl<>(Registry.BLOCK_ENTITY_TYPE);
    }

    public static Registrar<EntityType<?>> entity() {
        return new RegistrarImpl<>(Registry.ENTITY_TYPE);
    }

    public static Registrar<ScreenHandlerType<?>> menu() {
        return new RegistrarImpl<>(Registry.SCREEN_HANDLER);
    }

    public static Registrar<SoundEvent> soundEvent() {
        return new RegistrarImpl<>(Registry.SOUND_EVENT);
    }
}
