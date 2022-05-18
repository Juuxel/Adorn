package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.util.logger
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = logger()

    override fun open(player: PlayerEntity, factory: NamedMenuFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (!player.world.isClient) {
            // ^ technically not needed as vanilla safeguards against it,
            // but no need to create the extra factory on the client
            player.openMenu(
                object : ExtendedScreenHandlerFactory, NamedMenuFactory by factory {
                    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
                        buf.writeBlockPos(pos)
                    }
                }
            )
        }
    }

    override fun <M : Menu> createType(factory: (syncId: Int, inventory: PlayerInventory, buf: PacketByteBuf) -> M): MenuType<M> =
        ExtendedScreenHandlerType(factory)
}
