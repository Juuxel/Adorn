package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.client.CottonScreen
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import net.minecraft.entity.player.PlayerEntity

class DrawerScreen(
    controller: DrawerController,
    player: PlayerEntity
) : CottonScreen<DrawerController>(controller, player)

class KitchenCupboardScreen(
    controller: KitchenCupboardController,
    player: PlayerEntity
) : CottonScreen<KitchenCupboardController>(controller, player)
