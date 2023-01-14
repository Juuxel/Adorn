package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.Element

class FlipBook<E : Element> : WidgetEnvelope, PageContainer<E> {
    private val pages = ArrayList<E>()
    override var currentPage = 0
        set(value) {
            field = value
            for (listener in pageUpdateListeners) {
                listener()
            }
        }
    override val pageCount get() = pages.size
    override val currentPageValue get() = current()
    private val pageUpdateListeners: MutableList<() -> Unit> = ArrayList()

    constructor()
    constructor(pageUpdateListener: () -> Unit) : this() {
        pageUpdateListeners += pageUpdateListener
    }

    override fun current() = pages[currentPage]

    fun add(page: E) {
        pages += page
    }

    fun addPageUpdateListener(listener: () -> Unit) {
        pageUpdateListeners += listener
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

    override fun showPage(value: E) {
        val index = pages.indexOf(value)
        if (index >= 0) {
            currentPage = index
        }
    }
}
