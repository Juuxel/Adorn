package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.text.Text

/**
 * A panel that shows one widget at a time like [a card panel][WCardPanel],
 * but allows the user to change the tabs with buttons.
 */
class WTabbedPanel : WPlainPanel() {
    private val buttonBar: WPlainPanel = WPlainPanel()
    private val cardPanel = WCardPanel()
    private val buttonGroup = WToggleableButton.Group()
    private var nextButtonX = 0

    init {
        _add(buttonBar, 0, 0, 9 * 18, 18)
        _add(cardPanel, 0, 30)
    }

    fun addTab(label: Text, tab: WWidget, buttonWidth: Int = 3 * 18 + 9) {
        cardPanel.addCard(tab)

        val button = WToggleableButton(label)
        button.setOnClick {
            select(tab)
        }
        button.setGroup(buttonGroup)
        buttonBar.add(button, nextButtonX, 0, buttonWidth, 18)
        nextButtonX += buttonWidth + 5
    }

    fun select(tab: WWidget) =
        cardPanel.select(tab)

    fun select(tab: Int) =
        cardPanel.select(tab)

    @Suppress("FunctionName")
    private fun _add(w: WWidget, x: Int, y: Int) = super.add(w, x, y)

    @Suppress("FunctionName")
    private fun _add(w: WWidget, x: Int, y: Int, width: Int, height: Int) = super.add(w, x, y, width, height)

    @Deprecated("Cannot add contents directly to a tabbed panel", level = DeprecationLevel.ERROR)
    override fun add(w: WWidget, x: Int, y: Int) {
        throw UnsupportedOperationException()
    }

    @Deprecated("Cannot add contents directly to a tabbed panel", level = DeprecationLevel.ERROR)
    override fun add(w: WWidget, x: Int, y: Int, width: Int, height: Int) {
        throw UnsupportedOperationException()
    }
}
