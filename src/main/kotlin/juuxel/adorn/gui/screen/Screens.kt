package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.client.CottonScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingStationController
import juuxel.adorn.gui.controller.TradingStationCustomerController
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

open class AdornScreen<C : CottonScreenController>(
    controller: C,
    player: PlayerEntity,
    title: Text
) : CottonScreen<C>(controller, player) {
    private val _title = title

    override fun getTitle() = _title
}

class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<DrawerController>(controller, player, title)

class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<KitchenCupboardController>(controller, player, title)

class TradingStationScreen(
    controller: TradingStationController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<TradingStationController>(controller, player, title)

class TradingStationCustomerScreen(
    controller: TradingStationCustomerController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<TradingStationCustomerController>(controller, player, title)
