package juuxel.adorn.platform.forge

import juuxel.adorn.platform.MenuBridge
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.network.NetworkHooks
import org.apache.logging.log4j.LogManager

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = LogManager.getLogger()

    override fun open(player: PlayerEntity, factory: NamedMenuFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (player is ServerPlayerEntity) {
            NetworkHooks.openGui(player, factory, pos)
        }
    }

    override fun <M : Menu> createType(factory: (Int, PlayerInventory, PacketByteBuf) -> M): MenuType<M> =
        IForgeMenuType.create(factory)
}
