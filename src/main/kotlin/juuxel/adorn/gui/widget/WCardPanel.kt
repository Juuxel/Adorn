package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import kotlin.math.max

/**
 * A panel that shows one widget at a time.
 *
 * Inspired by AWT's [java.awt.CardLayout].
 */
class WCardPanel : WPanel() {
    private var selectedIndex: Int = 0

    // Cards

    fun addCard(w: WWidget) {
        children += w

        w.setParent(this)
        w.setLocation(0, 0)
        expandToFit(w)
    }

    fun select(card: Int) {
        require(card >= 0 && card < children.size) { "card must be a valid child index" }
        selectedIndex = card
    }

    fun select(card: WWidget) =
        select(children.indexOf(card))

    // Mouse

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        val child = children[selectedIndex]
        return child.onMouseUp(x - child.x, y - child.y, button)
    }

    override fun onMouseDown(x: Int, y: Int, button: Int): WWidget {
        val child = children[selectedIndex]
        return child.onMouseDown(x - child.x, y - child.y, button)
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        val child = children[selectedIndex]
        child.onMouseDrag(x - child.x, y - child.y, button)
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        val child = children[selectedIndex]
        child.onClick(x - child.x, y - child.y, button)
    }

    override fun hit(x: Int, y: Int): WWidget {
        val child = children[selectedIndex]
        return child.hit(x - child.x, y - child.y)
    }

    // Layout

    override fun expandToFit(w: WWidget) {
        setSize(max(width, w.width), max(height, w.height))
    }

    override fun layout() {
        super.layout()
        children.forEach { it.setSize(width, height) }
    }

    // Painting

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        backgroundPainter?.paintBackground(x, y, this)

        if (selectedIndex < children.size) {
            val child = children[selectedIndex]
            child.paintBackground(x + child.x, y + child.y)
        }
    }

    @Environment(EnvType.CLIENT)
    override fun paintForeground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        if (selectedIndex < children.size) {
            val child = children[selectedIndex]
            child.paintForeground(x + child.x, y + child.y, mouseX, mouseY)
        }
    }
}
