package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.MenuBridge;
import juuxel.adorn.util.Logging;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class MenuBridgeImpl implements MenuBridge {
    public static final MenuBridgeImpl INSTANCE = new MenuBridgeImpl();
    private static final Logger LOGGER = Logging.logger();

    @Override
    public void open(Player player, @Nullable MenuProvider factory, BlockPos pos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", new Throwable("Stacktrace").fillInStackTrace());
            return;
        }

        if (!player.level().isClientSide()) {
            // ^ technically not needed as vanilla safeguards against it,
            // but no need to create the extra factory on the client

            player.openMenu(new ExtendedMenuProvider<>() {
                @Override
                public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
                    return factory.createMenu(syncId, playerInventory, player);
                }

                @Override
                public Component getDisplayName() {
                    return factory.getDisplayName();
                }

                @Override
                public BlockPos getScreenOpeningData(ServerPlayer player) {
                    return pos;
                }
            });
        }
    }

    @Override
    public <M extends AbstractContainerMenu, D> MenuType<M> createType(Factory<M, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> packetCodec) {
        return new ExtendedMenuType<>(factory::create, packetCodec);
    }
}
