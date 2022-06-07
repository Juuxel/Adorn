package juuxel.adorn.client.gui.screen

import juuxel.adorn.AdornCommon
import juuxel.adorn.menu.DrawerMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class DrawerScreen(menu: DrawerMenu, playerInventory: PlayerInventory, title: Text) : PalettedMenuScreen<DrawerMenu>(menu, playerInventory, title) {
    override val backgroundTexture = BACKGROUND_TEXTURE
    override val paletteId = PALETTE_ID

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/drawer.png")
        private val PALETTE_ID = AdornCommon.id("drawer")
    }
}
