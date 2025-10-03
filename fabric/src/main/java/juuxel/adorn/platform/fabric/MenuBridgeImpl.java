package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.MenuBridge;
import juuxel.adorn.util.Logging;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuType;
import net.minecraft.menu.NamedMenuFactory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class MenuBridgeImpl implements MenuBridge {
    public static final MenuBridgeImpl INSTANCE = new MenuBridgeImpl();
    private static final Logger LOGGER = Logging.logger();

    @Override
    public void open(PlayerEntity player, @Nullable NamedMenuFactory factory, BlockPos pos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", new Throwable("Stacktrace").fillInStackTrace());
            return;
        }

        if (!player.getEntityWorld().isClient()) {
            // ^ technically not needed as vanilla safeguards against it,
            // but no need to create the extra factory on the client

            player.openMenu(new ExtendedScreenHandlerFactory<>() {
                @Override
                public @Nullable Menu createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return factory.createMenu(syncId, playerInventory, player);
                }

                @Override
                public Text getDisplayName() {
                    return factory.getDisplayName();
                }

                @Override
                public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
                    return pos;
                }
            });
        }
    }

    @Override
    public <M extends Menu, D> MenuType<M> createType(Factory<M, D> factory, PacketCodec<? super RegistryByteBuf, D> packetCodec) {
        return new ExtendedScreenHandlerType<>(factory::create, packetCodec);
    }
}
