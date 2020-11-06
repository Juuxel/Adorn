package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.BookScreen
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents

class WPageTurnButton(private val pages: PageContainer, private val direction: Direction) : WPlainPanel() {
    override fun canResize() = true
    override fun setSize(x: Int, y: Int) {
        super.setSize(23, y)
    }

    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val enabled = when (direction) {
            Direction.PREVIOUS -> pages.hasPreviousPage()
            Direction.NEXT -> pages.hasNextPage()
        }

        if (!enabled) return
        val px = 1 / 256f
        val tx = if (isWithinBounds(mouseX, mouseY)) 23 else 0
        var ty = 192
        if (direction == Direction.PREVIOUS) {
            ty += 13
        }

        ScreenDrawing.texturedRect(x, y + height / 2 - 7, 23, 13, BookScreen.BOOK_TEXTURE, tx * px, ty * px, (tx + 23) * px, (ty + 13) * px, Colors.WHITE)
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        val enabled = when (direction) {
            Direction.PREVIOUS -> pages.hasPreviousPage()
            Direction.NEXT -> pages.hasNextPage()
        }

        if (enabled) {
            MinecraftClient.getInstance().soundManager.play(
                PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f)
            )

            when (direction) {
                Direction.PREVIOUS -> pages.showPreviousPage()
                Direction.NEXT -> pages.showNextPage()
            }
        }
    }

    enum class Direction {
        PREVIOUS, NEXT
    }
}
