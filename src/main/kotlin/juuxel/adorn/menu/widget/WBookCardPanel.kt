package juuxel.adorn.menu.widget

import io.github.cottonmc.cotton.gui.widget.WCardPanel

class WBookCardPanel : WCardPanel(), PageContainer {
    override var currentPage by ::selectedIndex
    override val pageCount by ::cardCount

    override fun showPreviousPage() {
        if (hasPreviousPage()) {
            selectedIndex--
        }
    }

    override fun showNextPage() {
        if (hasNextPage()) {
            selectedIndex++
        }
    }
}
