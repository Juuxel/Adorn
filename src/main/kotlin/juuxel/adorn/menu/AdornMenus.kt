package juuxel.adorn.menu

import juuxel.adorn.Adorn
import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object AdornMenus {
    val MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Adorn.NAMESPACE)

    val DRAWER = MENUS.register("drawer") { ScreenHandlerType(::DrawerMenu) }
    val KITCHEN_CUPBOARD = MENUS.register("kitchen_cupboard") { ScreenHandlerType(::KitchenCupboardMenu) }
    val TRADING_STATION = MENUS.register("trading_station") { ScreenHandlerType(::TradingStationMenu) }

    @OnlyIn(Dist.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        registerScreen(TRADING_STATION, ::TradingStationScreen)
    }

    private inline fun <M : ScreenHandler> registerScreen(
        type: RegistryObject<ScreenHandlerType<M>>,
        crossinline screenFn: (M, PlayerInventory, Text) -> HandledScreen<M>
    ) {
        HandledScreens.register(type.get()) { menu, inventory, title ->
            screenFn(menu, inventory, title)
        }
    }
}
