package juuxel.adorn.client.gui.screen

import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text

abstract class AdornMenuScreen<M : ScreenHandler>(
    menu: M,
    playerInventory: PlayerInventory,
    title: Text
) : HandledScreen<M>(menu, playerInventory, title) {
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, tickDelta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }
}
