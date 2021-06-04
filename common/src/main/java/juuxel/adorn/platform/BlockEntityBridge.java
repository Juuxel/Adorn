package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import juuxel.adorn.block.DrawerBlock;
import juuxel.adorn.block.KitchenCupboardBlock;
import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.TradingStationBlock;
import juuxel.adorn.block.entity.BlockEntityDescriptor;
import juuxel.adorn.block.entity.DrawerBlockEntity;
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity;
import juuxel.adorn.block.entity.ShelfBlockEntity;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import org.jetbrains.annotations.NotNull;

public final class BlockEntityBridge {
    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<ShelfBlock, ShelfBlockEntity> shelf() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<DrawerBlock, DrawerBlockEntity> drawer() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<KitchenCupboardBlock, KitchenCupboardBlockEntity> kitchenCupboard() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static BlockEntityDescriptor<TradingStationBlock, TradingStationBlockEntity> tradingStation() {
        return PlatformCore.expected();
    }
}
