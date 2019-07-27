package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WButton
import net.minecraft.client.gui.widget.AbstractButtonWidget
import net.minecraft.text.Text
import kotlin.math.min

/**
 * Toggleable and groupable buttons.
 * Can be grouped with [setGroup] and a [Group] instance.
 */
class WToggleableButton(private val label: Text) : WButton(label) {
    private var selected: Boolean = true
    private var group: Group? = null
    private var onClick: Runnable? = null

    init {
        super.setOnClick {
            group?.buttons?.forEach { it.selected = false }
            selected = true
            onClick?.run()
        }
    }

    fun setGroup(group: Group) {
        require(this.group == null) { "Group has already been set" }
        this.group = group

        if (group.buttons.any { it.selected })
            this.selected = false

        group.buttons += this
    }

    override fun setOnClick(r: Runnable) {
        this.onClick = r
    }

    inline fun setOnClick(crossinline fn: () -> Unit) =
        setOnClick(Runnable { fn() })

    override fun paintForeground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        if (!selected) {
            super.paintForeground(x, y, mouseX, mouseY)
        } else {
            val px = 1 / 256f

            val buttonLeft = 0*px
            val buttonTop = 46*px
            val halfWidth = min(width / 2, 198)
            val buttonWidth = halfWidth * px
            val buttonHeight = 20*px
            val buttonEndLeft = (200 - width / 2) * px

            ScreenDrawing.rect(
                AbstractButtonWidget.WIDGETS_LOCATION, x, y, width / 2, 20,
                buttonLeft, buttonTop,
                buttonLeft + buttonWidth, buttonTop + buttonHeight,
                -1
            )
            ScreenDrawing.rect(
                AbstractButtonWidget.WIDGETS_LOCATION, x + width / 2, y, width / 2, 20,
                buttonEndLeft, buttonTop,
                200 * px, buttonTop + buttonHeight,
                -1
            )

            ScreenDrawing.drawCenteredWithShadow(label.asFormattedString(), x + width / 2, y + (20 - 8) / 2, 0xA0A0A0)

            // WWidget.paintForeground()
            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
                renderTooltip(mouseX - x + getX(), mouseY - y + getY())
            }
        }
    }

    /**
     * A group of [toggleable buttons][WToggleableButton].
     */
    class Group {
        internal val buttons: MutableList<WToggleableButton> = ArrayList()
    }
}
