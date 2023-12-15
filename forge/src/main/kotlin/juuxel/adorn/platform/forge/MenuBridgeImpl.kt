package juuxel.adorn.platform.forge

import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.util.logger
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.NetworkHooks

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = logger()

    override fun open(player: PlayerEntity, factory: NamedMenuFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (player is ServerPlayerEntity) {
            NetworkHooks.openScreen(player, factory, pos)
        }
    }

    override fun <M : Menu> createType(factory: (Int, PlayerInventory, PacketByteBuf) -> M): MenuType<M> =
        IMenuTypeExtension.create(factory)
}
