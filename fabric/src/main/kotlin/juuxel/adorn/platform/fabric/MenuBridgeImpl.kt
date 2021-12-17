package juuxel.adorn.platform.fabric

import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.util.menuContextOf
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.LogManager

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = LogManager.getLogger()

    override fun drawer(syncId: Int, playerInventory: PlayerInventory, blockEntity: DrawerBlockEntity): ScreenHandler =
        DrawerMenu(syncId, playerInventory, menuContextOf(blockEntity))

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, blockEntity: KitchenCupboardBlockEntity) =
        KitchenCupboardMenu(syncId, playerInventory, menuContextOf(blockEntity))

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, blockEntity: TradingStationBlockEntity
    ): ScreenHandler = TradingStationMenu(syncId, playerInventory, menuContextOf(blockEntity), true)

    override fun open(player: PlayerEntity, factory: NamedScreenHandlerFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (!player.world.isClient) {
            // ^ technically not needed as vanilla safeguards against it,
            // but no need to create the extra factory on the client
            player.openHandledScreen(
                object : ExtendedScreenHandlerFactory, NamedScreenHandlerFactory by factory {
                    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
                        buf.writeBlockPos(pos)
                    }
                }
            )
        }
    }
}
