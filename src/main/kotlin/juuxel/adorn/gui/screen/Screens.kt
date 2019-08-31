package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.client.CottonScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingStationController
import net.minecraft.entity.player.PlayerEntity

open class AdornScreen<C : CottonScreenController>(
    controller: C,
    player: PlayerEntity
) : CottonScreen<C>(controller, player)

class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity
) : AdornScreen<DrawerController>(controller, player)

class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity
) : AdornScreen<KitchenCupboardController>(controller, player)

class TradingStationScreen(
    controller: TradingStationController,
    player: PlayerEntity
) : AdornScreen<TradingStationController>(controller, player)
