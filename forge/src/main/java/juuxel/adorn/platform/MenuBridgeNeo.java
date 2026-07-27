package juuxel.adorn.platform;

import juuxel.adorn.util.Logging;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public final class MenuBridgeNeo implements MenuBridge {
    public static final MenuBridgeNeo INSTANCE = new MenuBridgeNeo();
    private static final Logger LOGGER = Logging.logger();

    @Override
    public void open(Player player, @Nullable MenuProvider factory, BlockPos pos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", new Throwable("Stacktrace").fillInStackTrace());
            return;
        }

        player.openMenu(factory, pos);
    }

    @Override
    public <M extends AbstractContainerMenu, D> MenuType<M> createType(Factory<M, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> packetCodec) {
        return IMenuTypeExtension.create((syncId, playerInventory, buf) -> {
            D data = packetCodec.decode(buf);
            return factory.create(syncId, playerInventory, data);
        });
    }
}
