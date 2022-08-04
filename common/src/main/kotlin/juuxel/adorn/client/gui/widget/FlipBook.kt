package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.Element

class FlipBook(private val pageUpdateListener: () -> Unit) : WidgetEnvelope(), PageContainer<Element> {
    private val pages = ArrayList<Element>()
    override var currentPage = 0
        set(value) {
            field = value
            pageUpdateListener()
        }
    override val pageCount get() = pages.size
    override val currentPageValue get() = current()

    override fun current() = pages[currentPage]

    fun add(page: Element) {
        pages += page
    }

    override fun showPreviousPage() {
        if (hasPreviousPage()) {
            currentPage--
        }
    }

    override fun showNextPage() {
        if (hasNextPage()) {
            currentPage++
        }
    }
}
