package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.MenuBridge
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.LogManager

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = LogManager.getLogger()

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

    override fun <M : Menu> register(id: String, factory: (syncId: Int, inventory: PlayerInventory) -> M): Registered<MenuType<M>> {
        val type = ScreenHandlerRegistry.registerSimple(AdornCommon.id(id), factory)
        return Registered { type }
    }

    override fun <M : Menu> register(id: String, factory: (Int, PlayerInventory, PacketByteBuf) -> M): Registered<MenuType<M>> {
        val type = ScreenHandlerRegistry.registerExtended(AdornCommon.id(id), factory)
        return Registered { type }
    }
}
