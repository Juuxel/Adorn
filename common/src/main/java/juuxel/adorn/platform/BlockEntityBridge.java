package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import juuxel.adorn.block.entity.BlockEntityDescriptor;
import org.jetbrains.annotations.NotNull;

public final class BlockEntityBridge {
    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<?> shelf() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<?> drawer() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<?> kitchenCupboard() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<?> tradingStation() {
        return PlatformCore.expected();
    }
}
