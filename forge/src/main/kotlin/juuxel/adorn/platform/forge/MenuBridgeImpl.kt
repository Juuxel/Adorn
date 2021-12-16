package juuxel.adorn.platform.forge

import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.platform.forge.menu.DrawerMenu
import juuxel.adorn.platform.forge.menu.KitchenCupboardMenu
import juuxel.adorn.platform.forge.menu.TradingStationMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.network.NetworkHooks
import org.apache.logging.log4j.LogManager

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = LogManager.getLogger()

    override fun drawer(syncId: Int, playerInventory: PlayerInventory, blockEntity: DrawerBlockEntity) =
        DrawerMenu(syncId, playerInventory, blockEntity, menuContextOf(blockEntity))

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, blockEntity: KitchenCupboardBlockEntity) =
        KitchenCupboardMenu(syncId, playerInventory, blockEntity, menuContextOf(blockEntity))

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, blockEntity: TradingStationBlockEntity, owner: Boolean
    ): ScreenHandler? {
        if (!owner) return null // this is something we only support on fabric
        return TradingStationMenu(syncId, playerInventory, menuContextOf(blockEntity))
    }

    override fun open(player: PlayerEntity, factory: NamedScreenHandlerFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (player is ServerPlayerEntity) {
            NetworkHooks.openGui(player, factory, pos)
        }
    }
}
