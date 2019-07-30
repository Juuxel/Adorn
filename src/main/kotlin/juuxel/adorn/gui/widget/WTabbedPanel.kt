package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WPanel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.text.Text

/**
 * A panel that shows one widget at a time like [a card panel][WCardPanel],
 * but allows the user to change the tabs with buttons.
 */
class WTabbedPanel : WPanel() {
    private val buttonBar: WPlainPanel = WPlainPanel()
    private val cardPanel = WCardPanel()
    private val buttonGroup = WToggleableButton.Group()
    private var nextButtonX = 0

    init {
        val panel = WPlainPanel()
        panel.add(buttonBar, 0, 0, 9 * 18, 18)
        panel.add(cardPanel, 0, 30)

        children.add(panel)
        panel.setLocation(0, 0)
        panel.setParent(this)
        expandToFit(panel)
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
}
