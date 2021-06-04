package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

object MenuBridge {
    @JvmStatic
    @ExpectPlatform
    fun createDrawerMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext
    ): ScreenHandler = expected

    @JvmStatic
    @ExpectPlatform
    fun createKitchenCupboardMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext
    ): ScreenHandler = expected

    @JvmStatic
    @ExpectPlatform
    fun createTradingStationMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean
    ): ScreenHandler = expected
}
