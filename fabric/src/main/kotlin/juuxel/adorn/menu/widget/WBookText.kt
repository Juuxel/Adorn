package juuxel.adorn.menu.widget

import io.github.cottonmc.cotton.gui.widget.WText
import io.github.cottonmc.cotton.gui.widget.data.InputResult
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text

class WBookText(text: Text, val pages: PageContainer? = null) : WText(text) {
    init {
        disableDarkmode()
    }

    @Environment(EnvType.CLIENT)
    override fun onClick(x: Int, y: Int, button: Int): InputResult {
        if (button == 0) {
            val hovered = getTextStyleAt(x, y)
            if (hovered != null) {
                val clickEvent = hovered.clickEvent
                if (clickEvent != null && clickEvent.action == ClickEvent.Action.CHANGE_PAGE) {
                    if (pages != null) {
                        val page = clickEvent.value.toIntOrNull() ?: return InputResult.IGNORED
                        val pageIndex = page - 1
                        if (pageIndex >= 0 && pageIndex < pages.pageCount) {
                            pages.currentPage = pageIndex
                            MinecraftClient.getInstance().soundManager.play(
                                PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f)
                            )
                        }
                    }
                } else {
                    return super.onClick(x, y, button)
                }
            }
        }

        return InputResult.IGNORED
    }
}
