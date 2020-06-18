package juuxel.adorn.client.gui.screen

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingStationController
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
open class AdornScreen<T : SyncedGuiDescription>(
    description: T,
    player: PlayerEntity,
    title: Text
) : CottonInventoryScreen<T>(description, player, title)

@Environment(EnvType.CLIENT)
class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<DrawerController>(controller, player, title)

@Environment(EnvType.CLIENT)
class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<KitchenCupboardController>(controller, player, title)

@Environment(EnvType.CLIENT)
class TradingStationScreen(
    controller: TradingStationController,
    player: PlayerEntity,
    title: Text
) : AdornScreen<TradingStationController>(controller, player, title)
