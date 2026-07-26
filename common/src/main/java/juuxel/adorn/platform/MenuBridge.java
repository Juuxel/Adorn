package juuxel.adorn.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface MenuBridge {
    /**
     * Opens a menu with a pos with the opening NBT sent to the client.
     * Does nothing on the client.
     */
    void open(Player player, @Nullable MenuProvider factory, BlockPos pos);

    <M extends AbstractContainerMenu, D> MenuType<M> createType(Factory<M, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> packetCodec);

    @FunctionalInterface
    interface Factory<M extends AbstractContainerMenu, D> {
        M create(int syncId, Inventory inventory, D data);
    }
}
