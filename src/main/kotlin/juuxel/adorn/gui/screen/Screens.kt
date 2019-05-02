package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.client.CottonScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingTableController
import net.minecraft.entity.player.PlayerEntity

class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity
) : CottonScreen<DrawerController>(controller, player)

class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity
) : CottonScreen<KitchenCupboardController>(controller, player)

class TradingTableScreen(
    controller: TradingTableController,
    player: PlayerEntity
) : CottonScreen<TradingTableController>(controller, player)
