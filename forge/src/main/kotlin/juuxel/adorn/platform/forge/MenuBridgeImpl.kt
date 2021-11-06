package juuxel.adorn.platform.forge

import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.platform.forge.menu.DrawerMenu
import juuxel.adorn.platform.forge.menu.KitchenCupboardMenu
import juuxel.adorn.platform.forge.menu.TradingStationMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler

object MenuBridgeImpl : MenuBridge {
    override fun drawer(syncId: Int, playerInventory: PlayerInventory, blockEntity: DrawerBlockEntity) =
        DrawerMenu(syncId, playerInventory, blockEntity)

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, blockEntity: KitchenCupboardBlockEntity) =
        KitchenCupboardMenu(syncId, playerInventory, blockEntity)

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, blockEntity: TradingStationBlockEntity, owner: Boolean
    ): ScreenHandler? {
        if (!owner) return null // this is something we only support on fabric
        return TradingStationMenu(syncId, playerInventory, menuContextOf(blockEntity))
    }
}
