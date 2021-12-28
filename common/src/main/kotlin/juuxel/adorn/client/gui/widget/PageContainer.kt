package juuxel.adorn.client.gui.widget

interface PageContainer {
    var currentPage: Int // 0-indexed
    val pageCount: Int

    fun hasPreviousPage(): Boolean = currentPage > 0
    fun hasNextPage(): Boolean = currentPage < pageCount - 1

    fun showPreviousPage()
    fun showNextPage()
}
