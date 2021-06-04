package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import juuxel.adorn.lib.Registered;
import kotlin.jvm.functions.Function0;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Registrar<T> {
    @NotNull
    <U extends T> Registered<@NotNull U> register(String id, Function0<@NotNull U> provider);

    @NotNull
    default <@Nullable U extends T> Registered<U> registerOptional(String id, Function0<U> provider) {
        U value = provider.invoke();

        if (value == null) {
            return Registered.of(() -> null);
        }

        return register(id, () -> value);
    }

    @PlatformOnly(PlatformOnly.FORGE)
    default void execute(Object modBus) {}

    @ExpectPlatform
    @NotNull
    static Registrar<Block> block() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    static Registrar<Item> item() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    static Registrar<BlockEntityType<?>> blockEntity() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    static Registrar<EntityType<?>> entity() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    static Registrar<ScreenHandlerType<?>> menu() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    static Registrar<SoundEvent> soundEvent() {
        return PlatformCore.expected();
    }
}
