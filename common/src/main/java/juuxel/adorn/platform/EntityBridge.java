package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import juuxel.adorn.entity.SeatEntity;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public final class EntityBridge {
    @ExpectPlatform
    @NotNull
    public static EntityType<SeatEntity> createSeatType() {
        return PlatformCore.expected();
    }
}
