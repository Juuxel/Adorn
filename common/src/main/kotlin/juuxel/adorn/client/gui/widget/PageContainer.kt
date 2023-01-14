package juuxel.adorn.client.gui.widget

interface PageContainer<T> {
    var currentPage: Int // 0-indexed
    val pageCount: Int
    val currentPageValue: T

    fun hasPreviousPage(): Boolean = currentPage > 0
    fun hasNextPage(): Boolean = currentPage < pageCount - 1

    fun showPreviousPage()
    fun showNextPage()

    fun showPage(value: T)
}
