package juuxel.adorn.client.gui.screen

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
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
    menu: DrawerMenu,
    player: PlayerEntity,
    title: Text
) : AdornScreen<DrawerMenu>(menu, player, title)

@Environment(EnvType.CLIENT)
class KitchenCupboardScreen(
    menu: KitchenCupboardMenu,
    player: PlayerEntity,
    title: Text
) : AdornScreen<KitchenCupboardMenu>(menu, player, title)
