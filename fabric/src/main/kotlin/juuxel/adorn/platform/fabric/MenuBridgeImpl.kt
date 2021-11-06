package juuxel.adorn.platform.fabric

import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.util.menuContextOf
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler

object MenuBridgeImpl : MenuBridge {
    override fun drawer(syncId: Int, playerInventory: PlayerInventory, blockEntity: DrawerBlockEntity): ScreenHandler =
        DrawerMenu(syncId, playerInventory, menuContextOf(blockEntity))

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, blockEntity: KitchenCupboardBlockEntity) =
        KitchenCupboardMenu(syncId, playerInventory, menuContextOf(blockEntity))

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, blockEntity: TradingStationBlockEntity, owner: Boolean
    ): ScreenHandler = TradingStationMenu(syncId, playerInventory, menuContextOf(blockEntity), owner)
}
