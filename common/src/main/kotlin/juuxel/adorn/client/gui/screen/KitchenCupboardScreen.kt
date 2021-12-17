package juuxel.adorn.client.gui.screen

import juuxel.adorn.AdornCommon
import juuxel.adorn.menu.KitchenCupboardMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class KitchenCupboardScreen(menu: KitchenCupboardMenu, playerInventory: PlayerInventory, title: Text) :
    PalettedMenuScreen<KitchenCupboardMenu>(menu, playerInventory, title) {
    override val backgroundTexture = BACKGROUND_TEXTURE
    override val paletteId = PALETTE_ID

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/kitchen_cupboard.png")
        private val PALETTE_ID = AdornCommon.id("kitchen_cupboard")
    }
}
