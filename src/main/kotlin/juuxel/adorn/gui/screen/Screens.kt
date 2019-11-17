package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.CottonCraftingController
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingStationController
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity

@Environment(EnvType.CLIENT)
open class AdornScreen<C : CottonCraftingController>(
    controller: C,
    player: PlayerEntity
) : CottonInventoryScreen<C>(controller, player)

@Environment(EnvType.CLIENT)
class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity
) : AdornScreen<DrawerController>(controller, player)

@Environment(EnvType.CLIENT)
class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity
) : AdornScreen<KitchenCupboardController>(controller, player)

@Environment(EnvType.CLIENT)
class TradingStationScreen(
    controller: TradingStationController,
    player: PlayerEntity
) : AdornScreen<TradingStationController>(controller, player)
